package com.vromanyu.spring_security_jwt_v2.config;

import com.vromanyu.spring_security_jwt_v2.handlers.CustomAuthenticationExceptionHandler;
import com.vromanyu.spring_security_jwt_v2.handlers.CustomAuthorizationExceptionHandler;
import com.vromanyu.spring_security_jwt_v2.handlers.JwtAuthenticationFilter;
import com.vromanyu.spring_security_jwt_v2.handlers.JwtRefresherFilter;
import com.vromanyu.spring_security_jwt_v2.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

 private final MyUserDetailsService myUserDetailsService;
 private final JwtAuthenticationFilter jwtAuthenticationFilter;
 private final JwtRefresherFilter jwtRefresherFilter;

 @Autowired
 public SecurityConfiguration(MyUserDetailsService myUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtRefresherFilter jwtRefresherFilter) {
  this.myUserDetailsService = myUserDetailsService;
  this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  this.jwtRefresherFilter = jwtRefresherFilter;
 }

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http
   .csrf(AbstractHttpConfigurer::disable)
   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(req -> {
    req.requestMatchers("/home", "/register/**", "/authenticate").permitAll();
    req.requestMatchers("/func/call").permitAll();
    req.requestMatchers("/admin/**").hasRole("ADMIN");
    req.requestMatchers("/user/**").hasRole("USER");
    req.anyRequest().authenticated();
   })
   .exceptionHandling(exception -> {
    exception.authenticationEntryPoint(new CustomAuthenticationExceptionHandler());
    exception.accessDeniedHandler(new CustomAuthorizationExceptionHandler());
   })
   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
   .addFilterBefore(jwtRefresherFilter, UsernamePasswordAuthenticationFilter.class)
   .formLogin(AbstractAuthenticationFilterConfigurer::disable)
   .logout(AbstractHttpConfigurer::disable)
   .httpBasic(Customizer.withDefaults())
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
