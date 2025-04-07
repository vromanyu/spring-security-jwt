package com.vromanyu.spring_security_jwt_v2.controller;

import com.vromanyu.spring_security_jwt_v2.dto.MyUserDTO;
import com.vromanyu.spring_security_jwt_v2.service.JwtService;
import com.vromanyu.spring_security_jwt_v2.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

 private final AuthenticationManager authenticationManager;
 private final JwtService jwtService;
 private final MyUserDetailsService myUserDetailsService;

 @Autowired
 public ContentController(AuthenticationManager authenticationManager, JwtService jwtService, MyUserDetailsService myUserDetailsService) {
  this.authenticationManager = authenticationManager;
  this.jwtService = jwtService;
  this.myUserDetailsService = myUserDetailsService;
 }

 @GetMapping("/home")
 public String handleWelcome() {return "Welcome to home!";}

 @GetMapping("/admin/home")
 public String handleAdminHome() {return "Welcome to admin home!";}

 @GetMapping("/user/home")
 public String handleUserHome() {return "Welcome to user home!";}

 @PostMapping("/login")
 public String authenticateAndGetToken(@RequestBody MyUserDTO form) {
  Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.username(), form.password()));
  if (authentication.isAuthenticated()) {
   return jwtService.generateToken(myUserDetailsService.loadUserByUsername(form.username()));
  } else throw new UsernameNotFoundException("invalid username or password");
 }

}
