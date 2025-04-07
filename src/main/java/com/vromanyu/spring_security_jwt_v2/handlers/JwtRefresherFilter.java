package com.vromanyu.spring_security_jwt_v2.handlers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtRefresherFilter extends OncePerRequestFilter {

 private final Logger logger = LoggerFactory.getLogger(JwtRefresherFilter.class);

 @Override
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
  logger.info("request refresh attribute: {}", request.getAttribute("refresh"));
  filterChain.doFilter(request, response);
 }
}
