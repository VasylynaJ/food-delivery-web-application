package com.vasylyna.fooddelivery.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.vasylyna.fooddelivery.user.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTest {
    @Test void registrationHashesPasswordAndAssignsCustomerRole() {
        AppUserRepository users=mock(AppUserRepository.class);PasswordEncoder encoder=mock(PasswordEncoder.class);
        AuthenticationManager manager=mock(AuthenticationManager.class);JwtService jwt=mock(JwtService.class);
        when(users.existsByEmailIgnoreCase("person@example.com")).thenReturn(false);
        when(encoder.encode("strong-pass-1")).thenReturn("bcrypt-hash");
        when(users.save(any(AppUser.class))).thenAnswer(call->{AppUser u=call.getArgument(0);return u;});
        when(jwt.createToken(any())).thenReturn("signed-token");
        var result=new AuthService(users,encoder,manager,jwt).register(new AuthDtos.RegisterRequest("Test Person","person@example.com","strong-pass-1"));
        assertThat(result.token()).isEqualTo("signed-token");assertThat(result.role()).isEqualTo("CUSTOMER");
        verify(encoder).encode("strong-pass-1");verify(users).save(argThat(u->u.getPasswordHash().equals("bcrypt-hash")&&u.getRole()==Role.CUSTOMER));
    }
}
