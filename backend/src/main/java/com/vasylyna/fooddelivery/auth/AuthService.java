package com.vasylyna.fooddelivery.auth;

import com.vasylyna.fooddelivery.auth.dto.AuthResponse;
import com.vasylyna.fooddelivery.auth.dto.LoginRequest;
import com.vasylyna.fooddelivery.auth.dto.RegisterRequest;
import com.vasylyna.fooddelivery.user.AppUser;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import com.vasylyna.fooddelivery.user.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwt;

    public AuthService(AppUserRepository users, PasswordEncoder encoder, AuthenticationManager manager, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.manager = manager;
        this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        AppUser user = users.save(new AppUser(request.fullName().trim(), request.email().trim().toLowerCase(),
                encoder.encode(request.password()), Role.CUSTOMER));
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email().trim().toLowerCase(), request.password()));
        return response(users.findByEmailIgnoreCase(request.email()).orElseThrow());
    }

    private AuthResponse response(AppUser user) {
        return new AuthResponse(jwt.createToken(user), "Bearer", user.getId(), user.getFullName(),
                user.getEmail(), user.getRole().name());
    }
}
