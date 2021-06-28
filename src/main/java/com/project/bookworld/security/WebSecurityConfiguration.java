package com.project.bookworld.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

  @Override
  public void configure(HttpSecurity http) {
    try {
      http.csrf().disable().authorizeRequests().anyRequest().permitAll();
      logger.info("Requests are authroized");
    } catch (Exception e) {
      logger.error("UnAuthorized requests");
    }
  }
}
