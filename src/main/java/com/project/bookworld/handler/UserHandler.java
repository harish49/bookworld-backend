package com.project.bookworld.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.PlaceOrder;
import com.project.bookworld.dto.UserRequestResponse;
import com.project.bookworld.service.UserService;

/**
 * @author Harish Vemula
 *     <p>Controller to handle user related operations
 */
@RestController
@RequestMapping(BookWorldConstants.USERS)
@SuppressWarnings({"all"})
public class UserHandler {
  private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

  @Autowired UserService userService;

  @GetMapping("/all")
  public APIResponse getUsers() {
    APIResponse response = null;
    try {
      response = userService.getAllUsers();
    } catch (Exception e) {
      logger.error("Exception in /all route");
      e.printStackTrace();
    }
    return response;
  }

  @DeleteMapping("/remove/{username}")
  public APIResponse removeUser(@PathVariable("username") String username) {
    APIResponse response = null;
    try {
      response = userService.removeUser(username);
    } catch (Exception e) {
      logger.error("Exception in /remove route");
      e.printStackTrace();
    }
    return response;
  }

  @PostMapping("/login")
  public APIResponse userLogin(@RequestBody UserRequestResponse userLogin) {
    APIResponse response = null;
    try {
      response = userService.userLogin(userLogin);
    } catch (Exception e) {
      logger.error("Exception in /login route");
      e.printStackTrace();
    }
    return response;
  }

  @PostMapping("/signup")
  public APIResponse registerUser(@RequestBody UserRequestResponse user) {
    APIResponse response = null;
    try {
      response = userService.registerUser(user);
    } catch (Exception e) {
      logger.error("Exception in /signup route");
      e.printStackTrace();
    }
    return response;
  }

  @PutMapping("/updateprofile")
  public APIResponse updateProfile(@RequestBody UserRequestResponse profile) {
    APIResponse response = null;
    try {
      response = userService.updateProfile(profile);
    } catch (Exception e) {
      logger.error("Exception in /updateprofile route");
      e.printStackTrace();
    }
    return response;
  }

  @PostMapping("/placeorder")
  public APIResponse placeOrder(@RequestBody PlaceOrder order) {
    APIResponse response = null;
    try {
      response = userService.createOrder(order);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/order/{id}")
  public APIResponse getOrder(@PathVariable("id") String id) {
    APIResponse response = null;
    try {
      response = userService.getOrder(id);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return response;
  }

  @GetMapping("/orders/{username}")
  public APIResponse getOrders(@PathVariable("username") String username) {
    APIResponse response = null;
    try {
      response = userService.getOrders(username);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @PutMapping("/orders")
  public APIResponse updateOrder(@RequestBody PlaceOrder order) {
    APIResponse response = null;
    try {
      response = userService.updateOrder(order);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/ping")
  public APIResponse ping() {
    logger.info("Hitting Users resource");
    return new APIResponse().setResponseData("pong").setStatusCode(HttpStatus.OK.value());
  }
}
