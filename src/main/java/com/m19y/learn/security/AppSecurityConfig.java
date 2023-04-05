package com.m19y.learn.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.m19y.learn.role.AppUserRole.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class AppSecurityConfig {

  private static final String[] SECURED = {"/api/**"};
  private static final String[] UNSECURED = {
          "/",
          "/js/**",
          "index.html", // tidak bisa menggunakan "index" saja harus ditambahakan "index.html"
          "/css/**"
  };
  private final PasswordEncoder passwordEncoder;
  @Autowired
  public AppSecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(UNSECURED).permitAll() // berikan isi (TANPA PASSWORD) dengan url UNSECURED
            .requestMatchers(SECURED).hasRole(STUDENT.name()) // <- role based authentication (singular)
//            .requestMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.name())
//            .requestMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//            .requestMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//            .requestMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//            .requestMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) // (plural)
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and().build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails saya = User.withUsername("saya")
            .password(passwordEncoder.encode("password"))
//            .roles(ADMIN.name()) // ROLE_ADMIN
            .authorities(ADMIN.getGrantedAuthorities())
            .build();

    UserDetails dia = User.withUsername("dia")
            .password(passwordEncoder.encode("password"))
//            .roles(STUDENT.name()) // ROLE_STUDENT
            .authorities(STUDENT.getGrantedAuthorities())
            .build();

    UserDetails kamu = User.withUsername("kamu")
            .password(passwordEncoder.encode("password"))
//            .roles(ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
            .authorities(ADMINTRAINEE.getGrantedAuthorities())
            .build();


    return new InMemoryUserDetailsManager(saya, dia, kamu);
  }
}
