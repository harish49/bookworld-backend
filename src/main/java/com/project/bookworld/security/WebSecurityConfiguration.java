package com.project.bookworld.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.bookworld.service.UserDetailsSecurityService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

  @Autowired UserDetailsSecurityService userDetailsSecurityService;
  @Autowired JWTfilter filter;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsSecurityService);
  }

  @Override
  public void configure(HttpSecurity http) {
    try {
      //      http.csrf().disable().authorizeRequests().anyRequest().permitAll();
      http.cors()
          .and()
          .csrf()
          .disable()
          .authorizeRequests()
          .antMatchers("/books/**")
          .permitAll()
          .antMatchers("/users/**")
          .permitAll()
          .antMatchers("/jwt/token")
          .permitAll()
          .anyRequest()
          .authenticated()
          .and()
          .exceptionHandling()
          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
      logger.info("Requests are authroized");
    } catch (Exception e) {
      logger.error("UnAuthorized requests");
      e.printStackTrace();
    }
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder getEncoder() {
    logger.info("Getting BCryptPasswordEncoder instance");
    PasswordEncoder encoder = null;
    try {
      encoder = new BCryptPasswordEncoder();
    } catch (Exception e) {
      logger.error("Exception in getEncoder()");
      e.printStackTrace();
    }
    return encoder;
  }
}
