package com.project.bookworld.handler;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.JWTResponse;
import com.project.bookworld.dto.UserRequestResponse;
import com.project.bookworld.service.UserDetailsSecurityService;
import com.project.bookworld.utils.JWTUtils;

@RestController
@RequestMapping(BookWorldConstants.JWT)
public class JWTHandler {

  private static final Logger logger = LoggerFactory.getLogger(JWTHandler.class);

  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private UserDetailsSecurityService userDetailsService;
  @Autowired private JWTUtils jwtUtils;

  @PostMapping("/token")
  public APIResponse createJWTToken(@RequestBody UserRequestResponse login) {
    final JWTResponse jwtResponse = new JWTResponse();
    final APIResponse response = new APIResponse();
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(login.getUserName(), login.getPassword()));

    } catch (BadCredentialsException e) {
      logger.error(BookWorldConstants.INVALID_USERNAME_OR_PASSWORD);
      e.printStackTrace();
    }
    try {
      Optional<UserDetails> userDetails =
          Optional.of(userDetailsService.loadUserByUsername(login.getUserName()));
      final String jwtToken = jwtUtils.generateToken(userDetails.get());
      jwtResponse.setUserName(login.getUserName());
      jwtResponse.setToken(jwtToken);
      response.setStatusCode(HttpStatus.CREATED.value());
      response.setResponseData(jwtResponse);
      logger.info("JWT Token created successfully for user " + login.getUserName());
    } catch (Exception e) {
      logger.error("Exception in createJWTToken()");
      response
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .setResponseData(
              "Could not create JWT Token for successfully for user " + login.getUserName());
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/ping")
  public APIResponse testToken(@RequestBody UserRequestResponse login) {
    logger.info("Hitting JWT resource");
    return new APIResponse().setResponseData("pong");
  }
}
