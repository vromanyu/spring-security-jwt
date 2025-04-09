package com.vromanyu.spring_security_jwt.controller;

import com.vromanyu.spring_security_jwt.dto.LoginFormDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

 @PostMapping("/login")
 public String login(LoginFormDto loginFormDto) {
  return "Welcome to spring-security-jwt: " + loginFormDto.getUsername();
 }
}
