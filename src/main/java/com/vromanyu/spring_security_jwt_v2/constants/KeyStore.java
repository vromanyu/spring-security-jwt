package com.vromanyu.spring_security_jwt_v2.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public final class KeyStore {

 private final String key;

 @Autowired
 private KeyStore(Environment environment) {
  this.key = environment.getProperty("secret.key");
 }

 public String getKey() {
  return this.key;
 }

}
