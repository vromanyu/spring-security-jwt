package com.vromanyu.spring_security_jwt.filters;

import com.vromanyu.spring_security_jwt.service.KeyStoreService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JWTValidatorFilter extends OncePerRequestFilter {

 private KeyStoreService keyStoreService;

 public JWTValidatorFilter(KeyStoreService keyStoreService) {
  this.keyStoreService = keyStoreService;
 }

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
  String jwt = request.getHeader("Authorization");
  if (jwt == null || !jwt.startsWith("Bearer ")) {
   response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
   return;
  }
  jwt = jwt.substring(7);
  try {
   Key key = Keys.hmacShaKeyFor(keyStoreService.getKey().getBytes(StandardCharsets.UTF_8));
   Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
   Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities")));
   SecurityContextHolder.getContext().setAuthentication(authentication);
  } catch (Exception e) {
   response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
   return;
  }
   filterChain.doFilter(request, response);
 }

 @Override
 protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
  return request.getRequestURI().equals("/api/login");
 }

}
