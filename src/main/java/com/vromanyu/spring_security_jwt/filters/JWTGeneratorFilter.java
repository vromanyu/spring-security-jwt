package com.vromanyu.spring_security_jwt.filters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vromanyu.spring_security_jwt.service.KeyStoreService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JWTGeneratorFilter extends OncePerRequestFilter {

 private final KeyStoreService keyStoreService;
 private final AuthenticationManager authenticationManager;

 public JWTGeneratorFilter(KeyStoreService keyStoreService, AuthenticationManager authenticationManager) {
  this.keyStoreService = keyStoreService;
  this.authenticationManager = authenticationManager;
 }

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
  String body = request.getReader().lines().collect(Collectors.joining());
  ObjectMapper mapper = new ObjectMapper();
  try {
   Map<String, String> jsonMap = mapper.readValue(body, new TypeReference<>() {
   });
   Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jsonMap.get("username"), jsonMap.get("password")));
   ((UsernamePasswordAuthenticationToken) authentication).setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
   SecretKey key = Keys.hmacShaKeyFor(keyStoreService.getKey().getBytes(StandardCharsets.UTF_8));
   String token = Jwts.builder()
    .setIssuer("spring-security-jwt")
    .setSubject(jsonMap.get("username"))
    .setExpiration(Date.from(Instant.now().plusMillis(TimeUnit.MINUTES.toMillis(30))))
    .setIssuedAt(Date.from(Instant.now()))
    .claim("authorities", authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(",")))
    .signWith(key)
    .compact();
   response.setHeader("Authorization", "Bearer " + token);
   SecurityContextHolder.getContext().setAuthentication(authentication);
  } catch (Exception e) {
   response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
   return;
  }
  filterChain.doFilter(request, response);
 }

 @Override
 protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
  return !request.getServletPath().equals("/api/login");
 }

}
