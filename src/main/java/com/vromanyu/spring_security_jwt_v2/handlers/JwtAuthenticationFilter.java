package com.vromanyu.spring_security_jwt_v2.handlers;

import com.vromanyu.spring_security_jwt_v2.service.JwtService;
import com.vromanyu.spring_security_jwt_v2.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

 private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
 private final MyUserDetailsService myUserDetailsService;
 private final JwtService jwtService;

 @Autowired
 public JwtAuthenticationFilter(MyUserDetailsService myUserDetailsService, JwtService jwtService) {
  this.myUserDetailsService = myUserDetailsService;
  this.jwtService = jwtService;
 }

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
  String authHeader = request.getHeader("Authorization");
  if (authHeader == null || !authHeader.startsWith("Bearer ")) {
   logger.info("Bearer token not found");
   filterChain.doFilter(request, response);
   return;
  }
  String jwt = authHeader.substring(7);
  try {
   Claims claims = jwtService.parseTokenToClaims(jwt);
   String username = claims.getSubject();
   if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
   }
  } catch (Exception e) {
   request.getRequestDispatcher("/login").forward(request, response);
  } finally {
  filterChain.doFilter(request, response);
  }
 }

}
