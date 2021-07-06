package com.project.bookworld.service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.UserDetailsdto;
import com.project.bookworld.dto.UserRequestResponse;
import com.project.bookworld.entities.Users;
import com.project.bookworld.entities.UsersAccount;
import com.project.bookworld.repositories.UsersRepository;
import com.project.bookworld.security.WebSecurityConfiguration;

@Service
@SuppressWarnings({"all"})
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired private WebSecurityConfiguration configuration;
  @Autowired private UserDetailsSecurityService userDetailsSecurityService;
  @Autowired private UsersRepository usersRepo;

  public APIResponse getUsersFromDatabase() {
    logger.info("Getting users from database");
    Optional<List<Users>> users = Optional.ofNullable(null);
    final APIResponse response = new APIResponse();
    try {
      try {
        users = Optional.of(usersRepo.findAll());
      } catch (Exception e) {
        logger.error("Exception in getTestUsers()");
        buildAPIResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Exception occurred while fetching users from database",
            Collections.EMPTY_LIST,
            response);
        e.printStackTrace();
      }
      if (users.isPresent()) {
        buildAPIResponse(HttpStatus.OK.value(), null, users.get(), response);
        logger.info("Retrieving all users from database");
      } else {
        buildAPIResponse(
            HttpStatus.NO_CONTENT.value(),
            BookWorldConstants.NO_USERS_IN_DATABASE,
            Collections.EMPTY_LIST,
            response);
        logger.error(BookWorldConstants.NO_USERS_IN_DATABASE);
      }
    } catch (Exception e) {
      logger.error("Exception in getTestUsers()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Could not retrieve users from database",
          null,
          response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse userLogin(UserRequestResponse userLogin) {
    logger.info("Started to validate credentials for the user " + userLogin.getUserName());
    final APIResponse response = new APIResponse();
    try {
      final String username = userLogin.getUserName();
      UserDetailsdto user = null;
      try {
        user = (UserDetailsdto) userDetailsSecurityService.loadUserByUsername(username);
      } catch (Exception e) {
        buildAPIResponse(
            HttpStatus.BAD_REQUEST.value(),
            BookWorldConstants.USERNAME_NOT_FOUND.format(username),
            null,
            response);
        logger.error(BookWorldConstants.USERNAME_NOT_FOUND.format(username));
        e.printStackTrace();
        return response;
      }
      final String password = userLogin.getPassword();
      final boolean credentialsMatched =
          configuration.getEncoder().matches(password, user.getPassword());
      if (credentialsMatched) {
        userLogin.setPassword(null);
        buildAPIResponse(HttpStatus.ACCEPTED.value(), null, userLogin, response);
      } else {
        logger.error(BookWorldConstants.INVALID_USERNAME_OR_PASSWORD);
        buildAPIResponse(
            HttpStatus.BAD_REQUEST.value(),
            BookWorldConstants.INVALID_USERNAME_OR_PASSWORD,
            null,
            response);
      }
    } catch (Exception e) {
      logger.error("Exception in userLogin()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not login successfully", null, response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse registerUser(UserRequestResponse user) {
    logger.info("Registering the user..." + user.getUserName());
    final APIResponse response = new APIResponse();
    try {
      final String username = user.getUserName();
      Optional<Users> existingUserName =
          usersRepo
              .findAll()
              .stream()
              .filter(exisitingUser -> exisitingUser.getUserName().equals(username))
              .findFirst();
      if (existingUserName.isPresent()) {
        logger.error(BookWorldConstants.USERNAME_ALREADY_EXISTS.format(username));
        buildAPIResponse(
            HttpStatus.NOT_ACCEPTABLE.value(),
            BookWorldConstants.USERNAME_ALREADY_EXISTS.format(username),
            null,
            response);
      } else {
        final String password = user.getPassword();
        final String encodedPassword = configuration.getEncoder().encode(password);
        final Users newUser = new Users();
        newUser.setUserName(username);
        newUser.setPassword(encodedPassword);
        newUser.setIsAdmin("N");
        newUser.setEmail(user.getEmail());
        newUser.setMobileNumber(user.getMobileNumber());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setCreatedTs(new Timestamp(System.currentTimeMillis()));
        newUser.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
        UsersAccount newUsersAccount = new UsersAccount();
        newUsersAccount.setUserName(username);
        newUsersAccount.setListOfOrders(Collections.EMPTY_LIST);
        newUsersAccount.setListOfReviews(Collections.EMPTY_LIST);
        newUsersAccount.setCreatedTs(new Timestamp(System.currentTimeMillis()));
        newUsersAccount.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
        try {
          usersRepo.save(newUser);
          user.setPassword(null);
          buildAPIResponse(HttpStatus.CREATED.value(), null, user, response);
          logger.info(BookWorldConstants.ACCOUNT_CREATED_SUCCESSFULLY.format(username));
        } catch (Exception e) {
          logger.error("Exception while saving new user in database");
          buildAPIResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not create account", user, response);
          e.printStackTrace();
        }
        user.setPassword(null);
        buildAPIResponse(HttpStatus.CREATED.value(), null, user, response);
        logger.info(BookWorldConstants.ACCOUNT_CREATED_SUCCESSFULLY.format(username));
      }
    } catch (Exception e) {
      logger.error("Error in performing user Resitration for the user" + user.getUserName());
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Could not register the user" + user.getUserName(),
          user,
          response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse updateProfile(UserRequestResponse profile) {

    logger.info("Updating profile for user " + profile.getUserName());
    final APIResponse response = new APIResponse();
    try {
      final String username = profile.getUserName();
      final String password = profile.getPassword();
      final String email = profile.getEmail();
      final String firstName = profile.getFirstName();
      final String lastName = profile.getLastName();
      final String mobileNumber = profile.getMobileNumber();
      final Optional<Users> existingUser =
          usersRepo
              .findAll()
              .stream()
              .filter(exisitingUser -> exisitingUser.getUserName().equals(username))
              .findFirst();
      if (!existingUser.isPresent()) {
        logger.error(BookWorldConstants.USERNAME_NOT_FOUND.format(username));
        buildAPIResponse(HttpStatus.BAD_REQUEST.value(), "User does not exist", null, response);
      } else {
        existingUser.get().setUserName(username);
        if (password.length() > 0) {
          existingUser.get().setPassword(configuration.getEncoder().encode(password));
        }
        if (email.length() > 0) {
          existingUser.get().setEmail(email);
        }
        if (mobileNumber.length() > 0) {
          existingUser.get().setMobileNumber(mobileNumber);
        }
        if (firstName.length() > 0) {
          existingUser.get().setFirstName(firstName);
        }
        if (lastName.length() > 0) {
          existingUser.get().setLastName(lastName);
        }
        existingUser.get().setUpdatedTs(new Timestamp(System.currentTimeMillis()));
        try {
          usersRepo.save(existingUser.get());
          logger.info("Successfully updated profile for user " + username);
          profile.setPassword(null);
          response.setResponseData(profile).setStatusCode(HttpStatus.OK.value());
          buildAPIResponse(HttpStatus.OK.value(), null, profile, response);
        } catch (Exception e) {
          logger.error("Failed to update profile for user " + username);
          response
              .setError("Could not update details successfully")
              .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
          buildAPIResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not update profile", null, response);
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      logger.error("Exception in updateProfile()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not update profile", null, response);
      e.printStackTrace();
    }
    return response;
  }

  private void buildAPIResponse(
      final int statusCode,
      final String error,
      final Object responseData,
      final APIResponse response) {
    try {
      response.setStatusCode(statusCode).setError(error).setResponseData(responseData);

    } catch (Exception e) {
      logger.error("Exception in buildAPIResponse()");
    }
  }
}
