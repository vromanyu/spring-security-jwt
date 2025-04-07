package com.vromanyu.spring_security_jwt_v2.handlers;

import com.vromanyu.spring_security_jwt_v2.entity.MyUser;
import com.vromanyu.spring_security_jwt_v2.service.JwtService;
import com.vromanyu.spring_security_jwt_v2.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

 private final JwtService jwtService;
 private final MyUserDetailsService myUserDetailsService;

 @Autowired
 public JwtAuthenticationFilter(JwtService jwtService, MyUserDetailsService myUserDetailsService) {
  this.jwtService = jwtService;
  this.myUserDetailsService = myUserDetailsService;
 }

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
  String authHeader = request.getHeader("Authorization");
  if (authHeader == null || !authHeader.startsWith("Bearer ")) {
   filterChain.doFilter(request, response);
   return;
  }

  String jwt = authHeader.substring(7);
  String username = jwtService.extractUsername(jwt);
  if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
   UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
   if (userDetails != null && jwtService.isTokenValid(jwt)) {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    request.setAttribute("refresh", true);
   }
  };
  filterChain.doFilter(request, response);
 }
}
