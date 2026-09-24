package com.vasylyna.fooddelivery.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vasylyna.fooddelivery.auth.dto.LoginRequest;
import com.vasylyna.fooddelivery.auth.dto.RegisterRequest;
import com.vasylyna.fooddelivery.user.AppUser;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import com.vasylyna.fooddelivery.user.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

class AuthServiceTest {
    @Test
    void registrationHashesPasswordAndAssignsCustomerRole() {
        AppUserRepository users = mock(AppUserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtService jwt = mock(JwtService.class);
        when(users.existsByEmailIgnoreCase("person@example.com")).thenReturn(false);
        when(encoder.encode("strong-pass-1")).thenReturn("bcrypt-hash");
        when(users.save(any(AppUser.class))).thenAnswer(call -> call.getArgument(0));
        when(jwt.createToken(any())).thenReturn("signed-token");

        var result = service(users, encoder, manager, jwt)
                .register(new RegisterRequest("Test Person", "person@example.com", "strong-pass-1"));

        assertThat(result.token()).isEqualTo("signed-token");
        assertThat(result.role()).isEqualTo("CUSTOMER");
        verify(encoder).encode("strong-pass-1");
        verify(users).save(argThat(user -> user.getPasswordHash().equals("bcrypt-hash")
                && user.getRole() == Role.CUSTOMER));
    }

    @Test
    void loginReturnsTheExistingUserSessionAfterSuccessfulAuthentication() {
        AppUserRepository users = mock(AppUserRepository.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        AppUser user = new AppUser("Test Person", "person@example.com", "bcrypt-hash", Role.CUSTOMER);
        when(users.findByEmailIgnoreCase("person@example.com")).thenReturn(Optional.of(user));
        JwtService jwt = mock(JwtService.class);
        when(jwt.createToken(user)).thenReturn("signed-token");

        var result = service(users, mock(PasswordEncoder.class), manager, jwt)
                .login(new LoginRequest("person@example.com", "strong-pass-1"));

        assertThat(result.token()).isEqualTo("signed-token");
        assertThat(result.email()).isEqualTo("person@example.com");
        verify(manager).authenticate(any());
        verify(jwt).createToken(user);
    }

    @Test
    void loginRejectsInvalidCredentialsWithoutLookingUpOrIssuingAToken() {
        AppUserRepository users = mock(AppUserRepository.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("internal auth detail"));
        JwtService jwt = mock(JwtService.class);

        assertThatThrownBy(() -> service(users, mock(PasswordEncoder.class), manager, jwt)
                .login(new LoginRequest("person@example.com", "wrong-password")))
                .isInstanceOf(BadCredentialsException.class);

        verifyNoInteractions(users, jwt);
    }

    @Test
    void duplicateRegistrationKeepsConflictStatusAndSafeMessage() {
        AppUserRepository users = mock(AppUserRepository.class);
        when(users.existsByEmailIgnoreCase("person@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service(users, mock(PasswordEncoder.class),
                mock(AuthenticationManager.class), mock(JwtService.class))
                .register(new RegisterRequest("Test Person", "person@example.com", "strong-pass-1")))
                .isInstanceOfSatisfying(ResponseStatusException.class, exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(exception.getReason()).isEqualTo("Email is already registered");
                });

        verify(users, never()).save(any());
    }

    @Test
    void configuredPasswordEncoderUsesBcrypt() {
        PasswordEncoder encoder = new SecurityConfiguration().passwordEncoder();
        String hash = encoder.encode("strong-pass-1");

        assertThat(hash).startsWith("$2");
        assertThat(hash).isNotEqualTo("strong-pass-1");
        assertThat(encoder.matches("strong-pass-1", hash)).isTrue();
        assertThat(encoder.matches("wrong-password", hash)).isFalse();
    }

    private AuthService service(AppUserRepository users, PasswordEncoder encoder,
            AuthenticationManager manager, JwtService jwt) {
        return new AuthService(users, encoder, manager, jwt);
    }
}