package com.vromanyu.spring_security_jwt.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApplicationController {

 @GetMapping("/user")
 public String userEndpoint() {
  return "Welcome to the user endpoint. " + SecurityContextHolder.getContext().getAuthentication() + " Accessible only for authenticated 'user' roles.";
 }

 @GetMapping("/admin")
 public String adminEndpoint(){
  return "Welcome to the admin endpoint. " + SecurityContextHolder.getContext().getAuthentication() + " Accessible only for authenticated 'admin' roles.";
 }

 @GetMapping("/authenticated")
 public String publicEndpoint(){
  return "Welcome to the public endpoint. " + SecurityContextHolder.getContext().getAuthentication() + " Accessible for all authenticated roles.";
 }

}
