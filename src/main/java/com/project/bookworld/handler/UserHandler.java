package com.project.bookworld.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.Login;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.entities.Users;
import com.project.bookworld.service.UserService;

@RestController
@CrossOrigin
@RequestMapping(BookWorldConstants.USERS)
@SuppressWarnings({"all"})
public class UserHandler {
  private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

  @Autowired UserService userService;

  @GetMapping("/testusers")
  public ResponseEntity getTestUsers() {
    logger.info("Getting default users from database");
    APIResponse response = null;
    try {
      response = userService.getTestUsers();
    } catch (Exception e) {
      logger.error("Exception in getTestUsers()");
      e.printStackTrace();
    }
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity userLogin(@RequestBody Login userLogin) {
    APIResponse response = null;
    try {
      response = userService.userLogin(userLogin);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody Users user) {
    APIResponse response = null;
    try {
      response = userService.registerUser(user);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }
}
