package com.vasylyna.fooddelivery.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.vasylyna.fooddelivery.common.ApiErrorResponseWriter;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwt;private final UserDetailsService users;private final ApiErrorResponseWriter errors;
 public JwtAuthenticationFilter(JwtService jwt,UserDetailsService users,ApiErrorResponseWriter errors){this.jwt=jwt;this.users=users;this.errors=errors;}
 private static final Logger logger=LoggerFactory.getLogger(JwtAuthenticationFilter.class);
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
  String header=request.getHeader("Authorization");
  if(header!=null&&header.startsWith("Bearer ")&&SecurityContextHolder.getContext().getAuthentication()==null){
   try{var details=users.loadUserByUsername(jwt.subject(header.substring(7)));var auth=new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));SecurityContextHolder.getContext().setAuthentication(auth);}catch(JwtException | org.springframework.security.core.userdetails.UsernameNotFoundException invalidToken){SecurityContextHolder.clearContext();errors.write(response,org.springframework.http.HttpStatus.UNAUTHORIZED,"Invalid or expired token");return;}catch(RuntimeException internalError){SecurityContextHolder.clearContext();logger.error("Unexpected error while resolving bearer token",internalError);errors.write(response,org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred");return;}
  }
  chain.doFilter(request,response);
 }
}
