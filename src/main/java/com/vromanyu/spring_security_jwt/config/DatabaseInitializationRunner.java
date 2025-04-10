package com.vromanyu.spring_security_jwt.config;

import com.vromanyu.spring_security_jwt.entity.MyUser;
import com.vromanyu.spring_security_jwt.repository.MyUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializationRunner implements CommandLineRunner {

 private MyUserRepository myUserRepository;
 private PasswordEncoder passwordEncoder;

 public DatabaseInitializationRunner(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
  this.myUserRepository = myUserRepository;
  this.passwordEncoder = passwordEncoder;
 }

 @Override
 public void run(String... args) throws Exception {
  MyUser user = new MyUser();
  user.setUsername("user");
  user.setPassword(passwordEncoder.encode("user"));
  user.setRole("USER");
  myUserRepository.save(user);
  MyUser admin = new MyUser();
  admin.setUsername("admin");
  admin.setPassword(passwordEncoder.encode("admin"));
  admin.setRole("ADMIN");
  myUserRepository.save(admin);
 }

}
