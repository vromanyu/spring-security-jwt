package com.vromanyu.spring_security_jwt.repository;

import com.vromanyu.spring_security_jwt.entity.MyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MyUserRepository extends CrudRepository<MyUser, Integer> {
 Optional<MyUser> findByUsername(String username);
}
