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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.m19y.learn.role.AppUserRole.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class AppSecurityConfig {

  private static final String[] SECURED = {"/api/**"};
  private static final String[] COOKIES = {"remember-me","JSESSIONID"};
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
            // .csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())) // akan selalu minta csrfToke di cookies
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(UNSECURED).permitAll() // berikan isi (TANPA PASSWORD) dengan url UNSECURED
            .requestMatchers(SECURED).hasRole(STUDENT.name()) // <- role based authentication (singular)
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
              .loginPage("/login")
              .permitAll()
              .defaultSuccessUrl("/courses",true) // setelah selesai login, langsung ke course url
              .passwordParameter("password")
              .usernameParameter("username")
            .and()
            .rememberMe()// by default akan disimpan selama 30 menit, dengan menggunakan remmemberMe() akan disimpan selama 2 minggu,
              .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) // costome expiration
              .key("rahasia")// untuk menggenerate md5 hash
            .and()
            .logout()
              .logoutUrl("/logout")
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // kalau csrf disable, harus hapus ini
              .clearAuthentication(true)
              .invalidateHttpSession(true)
              .deleteCookies(COOKIES)
              .logoutSuccessUrl("/login")
            .and().build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails saya = User.withUsername("saya")
            .password(passwordEncoder.encode("password"))
            .authorities(ADMIN.getGrantedAuthorities())
            .build();

    UserDetails dia = User.withUsername("dia")
            .password(passwordEncoder.encode("password"))
            .authorities(STUDENT.getGrantedAuthorities())
            .build();

    UserDetails kamu = User.withUsername("kamu")
            .password(passwordEncoder.encode("password"))
            .authorities(ADMINTRAINEE.getGrantedAuthorities())
            .build();


    return new InMemoryUserDetailsManager(saya, dia, kamu);
  }
}
