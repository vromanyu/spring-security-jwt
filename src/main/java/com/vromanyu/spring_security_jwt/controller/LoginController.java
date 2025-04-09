package com.vromanyu.spring_security_jwt.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

 @PostMapping("/login")
 public String login() {
  return "Welcome to spring-security: " + SecurityContextHolder.getContext().getAuthentication();
 }

}
