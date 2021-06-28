package com.project.bookworld.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.entities.Users;
import com.project.bookworld.utils.BookUtils;

@RestController
@CrossOrigin
@RequestMapping(BookWorldConstants.USERS)
public class UserHandler {
  private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

  @GetMapping("/testusers")
  public List<Users> getTestUsers() {
    logger.info("Getting default users from database");
    Optional<List<Users>> users = null;
    try {
      users = Optional.of(BookUtils.getTestUsers());
    } catch (Exception e) {
      logger.error("Exception in getTestUsers()");
      e.printStackTrace();
    }
    return users.isPresent() ? users.get() : new ArrayList<Users>();
  }
}
