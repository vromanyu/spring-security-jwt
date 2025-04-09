package com.vromanyu.spring_security_jwt.filters;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class AfterAuthenticationLoggingFilter implements Filter {

 private final Logger logger = LoggerFactory.getLogger(this.getClass());

 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  if (auth != null) {
   logger.info("authentication : {}", auth);
   logger.info("username : {}", auth.getName());
   logger.info("password : {}", auth.getCredentials());
   logger.info("details : {}", auth.getDetails());
  }
  chain.doFilter(request, response);
 }
}
