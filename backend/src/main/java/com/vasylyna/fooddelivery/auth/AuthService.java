package com.vasylyna.fooddelivery.auth;

import com.vasylyna.fooddelivery.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service public class AuthService {
 private final AppUserRepository users;private final PasswordEncoder encoder;private final AuthenticationManager manager;private final JwtService jwt;
 public AuthService(AppUserRepository users,PasswordEncoder encoder,AuthenticationManager manager,JwtService jwt){this.users=users;this.encoder=encoder;this.manager=manager;this.jwt=jwt;}
 public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest r){if(users.existsByEmailIgnoreCase(r.email()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Email is already registered");AppUser u=users.save(new AppUser(r.fullName().trim(),r.email().trim().toLowerCase(),encoder.encode(r.password()),Role.CUSTOMER));return response(u);}
 public AuthDtos.AuthResponse login(AuthDtos.LoginRequest r){manager.authenticate(new UsernamePasswordAuthenticationToken(r.email().trim().toLowerCase(),r.password()));return response(users.findByEmailIgnoreCase(r.email()).orElseThrow());}
 private AuthDtos.AuthResponse response(AppUser u){return new AuthDtos.AuthResponse(jwt.createToken(u),"Bearer",u.getId(),u.getFullName(),u.getEmail(),u.getRole().name());}
}
