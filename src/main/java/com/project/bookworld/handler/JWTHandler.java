package com.project.bookworld.handler;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.Login;
import com.project.bookworld.service.UserDetailsSecurityService;
import com.project.bookworld.utils.JWTUtils;

@RestController
@CrossOrigin
@RequestMapping(BookWorldConstants.JWT)
public class JWTHandler {

  private static final Logger logger = LoggerFactory.getLogger(JWTHandler.class);

  @Autowired AuthenticationManager authenticationManager;
  @Autowired UserDetailsSecurityService userDetailsService;
  @Autowired JWTUtils jwtUtils;

  @PostMapping("/token")
  public ResponseEntity<?> createJWTToken(@RequestBody Login login) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

    } catch (BadCredentialsException e) {
      logger.error("Username or password is incorrect");
      e.printStackTrace();
    }
    Optional<UserDetails> userDetails =
        Optional.of(userDetailsService.loadUserByUsername(login.getUsername()));

    final String jwtToken = jwtUtils.generateToken(userDetails.get());
    return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
  }
}
