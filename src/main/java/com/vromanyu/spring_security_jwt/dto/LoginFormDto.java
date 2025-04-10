package com.vromanyu.spring_security_jwt.dto;

public class LoginFormDto {

 private String username;
 private String password;

 public LoginFormDto() {}

 public LoginFormDto(String username, String password) {
  this.username = username;
 }

 public String getUsername() {
  return username;
 }

 public void setUsername(String username) {
  this.username = username;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 @Override
 public String toString() {
  return "LoginFormDto{" +
   "username='" + username + '\'' +
   ", password='" + password + '\'' +
   '}';
 }
}
