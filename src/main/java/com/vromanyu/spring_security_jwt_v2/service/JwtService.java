package com.vromanyu.spring_security_jwt_v2.service;

import com.vromanyu.spring_security_jwt_v2.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

 private final static Logger logger = LoggerFactory.getLogger(JwtService.class);
 private static final long expiration = TimeUnit.MINUTES.toMillis(30);
 private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(ApplicationConstants.JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));

 public String generateToken(UserDetails user) {
  String token = Jwts.builder().setSubject(user.getUsername())
   .setIssuedAt(new Date())
   .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
   .signWith(SECRET_KEY)
   .setIssuer("spring-security-application")
   .claim("role", user.getAuthorities().toString())
   .compact();
  logger.info("generated token: {}", token);
  return token;
 }

 public static Claims parseTokenToClaims(String token) throws ExpiredJwtException {
  return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
 }

}
