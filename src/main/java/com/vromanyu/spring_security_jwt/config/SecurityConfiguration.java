package com.vromanyu.spring_security_jwt.config;

import com.vromanyu.spring_security_jwt.filters.*;
import com.vromanyu.spring_security_jwt.service.KeyStoreService;
import com.vromanyu.spring_security_jwt.service.MyUserDetailsService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http, KeyStoreService keyStoreService, MyUserDetailsService myUserDetailsService) throws Exception {
  return http
   .securityContext(conf -> conf.requireExplicitSave(true))
   .sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(req -> {
    req.requestMatchers("/api/login", "/error").permitAll();
    req.requestMatchers("/api/user").hasAnyAuthority("USER");
    req.requestMatchers("/api/admin").hasAnyAuthority("ADMIN");
    req.anyRequest().authenticated();
   })
   .cors(conf -> {
    conf.configurationSource(request -> {
     CorsConfiguration config = new CorsConfiguration();
     config.setAllowedOrigins(Collections.singletonList("*"));
     config.setAllowedMethods(Collections.singletonList("*"));
     config.setAllowedHeaders(Collections.singletonList("*"));
     config.setAllowCredentials(true);
     config.addExposedHeader("Authorization");
     config.setMaxAge(3600L);
     return config;
    });
   })
   .addFilterAfter(new JWTGeneratorFilter(keyStoreService, authenticationManager(myUserDetailsService)), UsernamePasswordAuthenticationFilter.class)
   .addFilterBefore(new JWTValidatorFilter(keyStoreService), UsernamePasswordAuthenticationFilter.class)
   .httpBasic(AbstractHttpConfigurer::disable)
   .formLogin(AbstractHttpConfigurer::disable)
   .csrf(AbstractHttpConfigurer::disable)
   .logout(AbstractHttpConfigurer::disable)
   .build();
 }

 @Bean
 public AuthenticationManager authenticationManager(MyUserDetailsService myUserDetailsService) throws Exception {
  return new ProviderManager(Collections.singletonList(new CustomAuthenticationProvider(myUserDetailsService, passwordEncoder())));
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
  return PasswordEncoderFactories.createDelegatingPasswordEncoder();
 }

}
