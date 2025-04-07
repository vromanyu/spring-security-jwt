package com.vromanyu.spring_security_jwt_v2.service;

import com.vromanyu.spring_security_jwt_v2.entity.MyUser;
import com.vromanyu.spring_security_jwt_v2.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

 private final MyUserRepository myUserRepository;

 @Autowired
 public MyUserDetailsService(MyUserRepository myUserRepository) {
  this.myUserRepository = myUserRepository;
 }

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  MyUser user = myUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user: " + username + " not found"));
  return User.builder().username(user.getUsername()).password(user.getPassword()).roles(getRoles(user)).build();
 }

 private String[] getRoles(MyUser myUser) {
  if (myUser.getRole() == null) {
   return new String[]{"USER"};
  }
  return myUser.getRole().split(",");
 }

}
