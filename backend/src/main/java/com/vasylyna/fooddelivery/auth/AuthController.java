package com.vasylyna.fooddelivery.auth;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService service;public AuthController(AuthService service){this.service=service;}
 @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public AuthDtos.AuthResponse register(@Valid @RequestBody AuthDtos.RegisterRequest r){return service.register(r);}
 @PostMapping("/login") public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest r){return service.login(r);}
}
