package com.vromanyu.spring_security_jwt_v2.service;

import com.vromanyu.spring_security_jwt_v2.constants.KeyStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

 private final KeyStore keyStore;
 private static final long expiration = TimeUnit.MINUTES.toMillis(30);

 @Autowired
 public JwtService(KeyStore keyStore) {
  this.keyStore = keyStore;
 }

 public String generateToken(UserDetails user) {
  SecretKey key = Keys.hmacShaKeyFor(keyStore.getKey().getBytes(StandardCharsets.UTF_8));
  return Jwts.builder().setSubject(user.getUsername())
   .setIssuedAt(new Date())
   .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
   .signWith(key)
   .setIssuer("spring-security-application")
   .claim("role", user.getAuthorities().toString())
   .compact();
 }

 public Claims parseTokenToClaims(String token) throws JwtException {
  SecretKey key = Keys.hmacShaKeyFor(keyStore.getKey().getBytes(StandardCharsets.UTF_8));
  return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
 }

}
