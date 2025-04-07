package com.vromanyu.spring_security_jwt_v2.config;

import com.vromanyu.spring_security_jwt_v2.service.MyUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

 private final MyUserDetailsService myUserDetailsService;
 private final PasswordEncoder passwordEncoder;

 public CustomAuthenticationProvider(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder) {
  this.myUserDetailsService = myUserDetailsService;
  this.passwordEncoder = passwordEncoder;
 }
 
 @Override
 public Authentication authenticate(Authentication authentication) throws AuthenticationException {
  UserDetails user = myUserDetailsService.loadUserByUsername(authentication.getName());
  if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
   return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
  } else throw new BadCredentialsException("Bad credentials");
 }

 @Override
 public boolean supports(Class<?> authentication) {
  return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
 }

}
