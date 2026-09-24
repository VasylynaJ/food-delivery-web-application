package com.vasylyna.fooddelivery.auth;

import com.vasylyna.fooddelivery.common.ApiErrorResponseWriter;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;

@Configuration
public class SecurityConfiguration {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{return configuration.getAuthenticationManager();}
 @Bean UserDetailsService userDetailsService(AppUserRepository users){return email->users.findByEmailIgnoreCase(email).map(u->User.withUsername(u.getEmail()).password(u.getPasswordHash()).roles(u.getRole().name()).build()).orElseThrow(()->new UsernameNotFoundException("User not found"));}
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthenticationFilter jwtFilter,ApiErrorResponseWriter errors)throws Exception{
  return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a
   .requestMatchers("/api/auth/**").permitAll().requestMatchers(HttpMethod.GET,"/api/restaurants/**").permitAll()
   .requestMatchers("/api/admin/**").hasRole("ADMIN").requestMatchers("/api/profile/**","/api/cart/**","/api/orders/**").authenticated().anyRequest().permitAll())
   .exceptionHandling(e->e
    .authenticationEntryPoint((request,response,exception)->errors.write(response,org.springframework.http.HttpStatus.UNAUTHORIZED,"Authentication required"))
    .accessDeniedHandler((request,response,exception)->errors.write(response,org.springframework.http.HttpStatus.FORBIDDEN,"Access denied")))
   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();
 }
 @Bean CorsConfigurationSource corsConfigurationSource(){CorsConfiguration c=new CorsConfiguration();c.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:3000"));c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type"));UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s;}
}
