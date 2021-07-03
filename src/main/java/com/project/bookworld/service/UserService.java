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
import com.project.bookworld.dto.Register;
import com.project.bookworld.dto.UserDetailsdto;
import com.project.bookworld.entities.Users;
import com.project.bookworld.repositories.UsersRepository;
import com.project.bookworld.security.WebSecurityConfiguration;
import com.project.bookworld.utils.BookUtils;

@Service
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired private BookUtils utils;
  @Autowired private WebSecurityConfiguration configuration;
  @Autowired private UserDetailsSecurityService userDetailsSecurityService;
  @Autowired UsersRepository usersRepo;

  public APIResponse getTestUsers() {
    logger.info("Getting default users from database");
    Optional<List<Users>> users = null;
    APIResponse response = new APIResponse();
    try {
      users = Optional.of(usersRepo.findAll());
      usersRepo.saveAll(users.get());
    } catch (Exception e) {
      logger.error("Exception in getTestUsers()");
      response
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .setResponseData(new ArrayList<Users>())
          .setError("Exception occurred while fetching users from database");
      e.printStackTrace();
    }
    if (!users.isPresent()) {
      users = Optional.of(new ArrayList<Users>());
    }
    if (users.isPresent()) {
      response.setStatusCode(HttpStatus.OK.value()).setResponseData(users.get());
    } else {
      response
          .setStatusCode(HttpStatus.NO_CONTENT.value())
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
      UserDetailsdto user = null;
      try {
        user = (UserDetailsdto) userDetailsSecurityService.loadUserByUsername(username);
      } catch (Exception e) {
        response.setStatusCode(HttpStatus.BAD_REQUEST.value()).setError("Invalid Username");
        logger.error("Invalid Username");
        e.printStackTrace();
        return response;
      }
      String password = userLogin.getPassword();
      logger.info(password);
      boolean credentialsMatched = configuration.getEncoder().matches(password, user.getPassword());
      if (credentialsMatched) {
        response.setStatusCode(HttpStatus.ACCEPTED.value()).setResponseData(user.getUser());
      } else {
        logger.info("hello");
        response
            .setStatusCode(HttpStatus.BAD_REQUEST.value())
            .setError("Invalid username or password");
      }
    } catch (Exception e) {
      logger.error("Exception in userLogin()");
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse registerUser(Register user) {
    logger.info("Registering the user..." + user.getUserName());
    APIResponse response = new APIResponse();
    try {
      String username = user.getUserName();
      Optional<Users> existingUserName =
          usersRepo
              .findAll()
              .stream()
              .filter(exisitingUser -> exisitingUser.getUserName().equals(username))
              .findFirst();
      if (existingUserName.isPresent()) {
        logger.error("UserName " + username + " is not available");
        response
            .setStatusCode(HttpStatus.NOT_ACCEPTABLE.value())
            .setError("Username " + username + " is not available");
      } else {
        String password = user.getPassword();
        String encodedPassword = configuration.getEncoder().encode(password);
        user.setPassword(encodedPassword);
        response
            .setStatusCode(HttpStatus.CREATED.value())
            .setResponseData("Successfully registered " + username);
        logger.info("Account for " + username + " successfully created");
      }
    } catch (Exception e) {
      logger.error("Error in performing user Resitration for the user" + user.getUserName());
      response
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .setError("Exception in registering the user" + user.getUserName());
      e.printStackTrace();
    }
    return response;
  }
}
