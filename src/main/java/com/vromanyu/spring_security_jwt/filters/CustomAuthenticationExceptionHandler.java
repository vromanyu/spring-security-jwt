package com.vromanyu.spring_security_jwt.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vromanyu.spring_security_jwt.exception.CustomAuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Date;

public class CustomAuthenticationExceptionHandler implements AuthenticationEntryPoint {

 @Override
 public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
  response.setContentType("application/json");
  String jsonResponse = new ObjectMapper().writeValueAsString(new CustomAuthenticationException(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), request.getRequestURI(), new Date()));
  response.getWriter().write(jsonResponse);
 }

}
