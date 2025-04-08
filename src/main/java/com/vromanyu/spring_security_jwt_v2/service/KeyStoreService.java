package com.vromanyu.spring_security_jwt_v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public final class KeyStoreService {

 private final String key;

 @Autowired
 private KeyStoreService(Environment environment) {
  this.key = environment.getProperty("secret.key");
 }

 public String getKey() {
  return this.key;
 }

}
