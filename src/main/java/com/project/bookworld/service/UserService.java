package com.project.bookworld.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.Login;
import com.project.bookworld.dto.UserDetailsdto;
import com.project.bookworld.entities.Users;
import com.project.bookworld.security.WebSecurityConfiguration;
import com.project.bookworld.utils.BookUtils;

@Service
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired private BookUtils utils;
  @Autowired private WebSecurityConfiguration configuration;
  @Autowired private UserDetailsSecurityService userDetailsSecurityService;

  public APIResponse getTestUsers() {
    logger.info("Getting default users from database");
    Optional<List<Users>> users = null;
    APIResponse response = new APIResponse();
    try {
      users = Optional.of(utils.getTestUsers());
    } catch (Exception e) {
      logger.error("Exception in getTestUsers()");
      response
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
          .setResponseData(new ArrayList<Users>())
          .setError("Exception occurred while fetching users from database");
      e.printStackTrace();
    }
    if (!users.isPresent()) {
      users = Optional.of(new ArrayList<Users>());
    }
    if (users.isPresent()) {
      response.setStatusCode(HttpStatus.OK).setResponseData(users.get());
    } else {
      response
          .setStatusCode(HttpStatus.NO_CONTENT)
          .setResponseData(users.get())
          .setError("No users are found in database");
    }
    return response;
  }

  public APIResponse userLogin(Login userLogin) {
    logger.info("Started to validate credentials for the user " + userLogin.getUsername());
    APIResponse response = new APIResponse();
    try {
      String username = userLogin.getUsername();
      UserDetailsdto user =
          (UserDetailsdto) userDetailsSecurityService.loadUserByUsername(username);
      String password = userLogin.getPassword();
      String encodedPassword = configuration.getEncoder().encode(password);
      boolean credentialsMatched = configuration.getEncoder().matches(password, encodedPassword);
      if (credentialsMatched) {
        response.setStatusCode(HttpStatus.ACCEPTED).setResponseData(user.getUser());
      } else {
        response.setStatusCode(HttpStatus.UNAUTHORIZED).setError("Invalid username or password");
      }
    } catch (Exception e) {
      logger.error("Exception in userLogin()");
      response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR).setError("Exception in userLogin()");
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse registerUser(Users user) {
    logger.info("Registering the user..." + user.getUserName());
    APIResponse response = new APIResponse();
    try {
      String username = user.getUserName();
      Optional<Users> existingUserName =
          utils
              .getTestUsers()
              .stream()
              .filter(exisitingUser -> exisitingUser.getUserName().equals(username))
              .findFirst();
      if (existingUserName.isPresent()) {
        logger.error("UserName " + username + " is not available");
        response
            .setStatusCode(HttpStatus.NOT_ACCEPTABLE)
            .setError("Username " + username + " is not available");
      } else {
        String password = user.getPassword();
        String encodedPassword = configuration.getEncoder().encode(password);
        user.setPassword(encodedPassword);
        response
            .setStatusCode(HttpStatus.CREATED)
            .setResponseData("Successfully registered " + username);
        logger.info("Account for " + username + " successfully created");
      }
    } catch (Exception e) {
      logger.error("Error in performing user Resitration for the user" + user.getUserName());
      response
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
          .setError("Exception in registering the user" + user.getUserName());
      e.printStackTrace();
    }
    return response;
  }
}
