package com.vromanyu.spring_security_jwt.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vromanyu.spring_security_jwt.exception.CustomAuthorizationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Date;

public class CustomAuthorizationExceptionHandler implements AccessDeniedHandler {

 @Override
 public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
  response.setContentType("application/json");
  String jsonResponse = new ObjectMapper().writeValueAsString(new CustomAuthorizationException(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), request.getRequestURI(), new Date()));
  response.getWriter().write(jsonResponse);
 }

}
