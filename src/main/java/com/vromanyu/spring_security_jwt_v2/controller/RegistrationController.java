package com.vromanyu.spring_security_jwt_v2.controller;

import com.vromanyu.spring_security_jwt_v2.entity.MyUser;
import com.vromanyu.spring_security_jwt_v2.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

 private final MyUserRepository myUserRepository;
 private final PasswordEncoder passwordEncoder;

 @Autowired
 public RegistrationController(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
  this.myUserRepository = myUserRepository;
  this.passwordEncoder = passwordEncoder;
 }

 @PostMapping("/register/user")
 public MyUser createUser(@RequestBody MyUser user){
  user.setPassword(passwordEncoder.encode(user.getPassword()));
  return myUserRepository.save(user);
 }
}
