package com.vromanyu.spring_security_jwt_v2.repository;

import com.vromanyu.spring_security_jwt_v2.entity.MyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MyUserRepository extends CrudRepository<MyUser, Integer> {
 Optional<MyUser> findByUsername(String username);
}
