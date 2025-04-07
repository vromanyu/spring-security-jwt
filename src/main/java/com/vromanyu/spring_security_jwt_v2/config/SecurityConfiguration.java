package com.vromanyu.spring_security_jwt_v2.config;

import com.vromanyu.spring_security_jwt_v2.handlers.CustomAuthenticationExceptionHandler;
import com.vromanyu.spring_security_jwt_v2.handlers.CustomAuthorizationExceptionHandler;
import com.vromanyu.spring_security_jwt_v2.handlers.JwtAuthenticationFilter;
import com.vromanyu.spring_security_jwt_v2.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

 private final MyUserDetailsService myUserDetailsService;
 private final JwtAuthenticationFilter jwtAuthenticationFilter;

 @Autowired
 public SecurityConfiguration(MyUserDetailsService myUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
  this.myUserDetailsService = myUserDetailsService;
  this.jwtAuthenticationFilter = jwtAuthenticationFilter;
 }

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http
   .csrf(AbstractHttpConfigurer::disable)
   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(req -> {
    req.requestMatchers("/home", "/register/**", "/authenticate", "/login").permitAll();
    req.requestMatchers("/func/call").permitAll();
    req.requestMatchers("/admin/**").hasRole("ADMIN");
    req.requestMatchers("/user/**").hasRole("USER");
    req.anyRequest().authenticated();
   })
   .exceptionHandling(exception -> {
    exception.authenticationEntryPoint(new CustomAuthenticationExceptionHandler());
    exception.accessDeniedHandler(new CustomAuthorizationExceptionHandler());
   })
   .addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
   .formLogin(AbstractHttpConfigurer::disable)
   .logout(AbstractHttpConfigurer::disable)
   .httpBasic(AbstractHttpConfigurer::disable)
   .build();
 }

 @Bean
 public AuthenticationManager authenticationManager() throws Exception {
  return new ProviderManager(Collections.singletonList(new CustomAuthenticationProvider(myUserDetailsService, passwordEncoder())));
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
  return PasswordEncoderFactories.createDelegatingPasswordEncoder();
 }

}
