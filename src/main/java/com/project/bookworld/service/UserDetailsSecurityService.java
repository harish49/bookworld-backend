package com.project.bookworld.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.bookworld.dto.UserDetailsdto;
import com.project.bookworld.entities.Users;
import com.project.bookworld.utils.BookUtils;

@Service
public class UserDetailsSecurityService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsSecurityService.class);
  @Autowired private BookUtils utils;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("Loading User by username" + username);
    Optional<Users> user = null;
    try {
      user =
          utils
              .getTestUsers()
              .stream()
              .filter(users -> users.getUserName().equals(username))
              .findFirst();

    } catch (Exception e) {
      logger.error("Exception in LoadingUserByUsername from database");
      e.printStackTrace();
    }
    if (user.isPresent()) {
      UserDetailsdto userDetailsdto = new UserDetailsdto(user.get());
      return userDetailsdto;
    }
    throw new UsernameNotFoundException("User not found!!");
  }
}
