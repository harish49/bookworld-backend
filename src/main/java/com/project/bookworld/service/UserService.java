package com.project.bookworld.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.PlaceOrder;
import com.project.bookworld.dto.UserDetailsdto;
import com.project.bookworld.dto.UserRequestResponse;
import com.project.bookworld.entities.Address;
import com.project.bookworld.entities.Book;
import com.project.bookworld.entities.OrderItem;
import com.project.bookworld.entities.OrderStatus;
import com.project.bookworld.entities.Orders;
import com.project.bookworld.entities.Payment;
import com.project.bookworld.entities.PaymentMode;
import com.project.bookworld.entities.PaymentStatus;
import com.project.bookworld.entities.Users;
import com.project.bookworld.entities.UsersAccount;
import com.project.bookworld.repositories.BookRepository;
import com.project.bookworld.repositories.OrdersRepository;
import com.project.bookworld.repositories.UsersAccountRepository;
import com.project.bookworld.repositories.UsersRepository;
import com.project.bookworld.security.WebSecurityConfiguration;

@Service
@SuppressWarnings({"all"})
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired private WebSecurityConfiguration configuration;
  @Autowired private UserDetailsSecurityService userDetailsSecurityService;
  @Autowired private UsersRepository usersRepo;
  @Autowired private UsersAccountRepository usersAccountRepo;
  @Autowired private OrdersRepository ordersRepo;
  @Autowired private BookRepository bookRepo;

  public APIResponse getUsersFromDatabase() {
    logger.info("Getting users from database");
    Optional<List<Users>> users = Optional.ofNullable(null);
    final APIResponse response = new APIResponse();
    try {
      try {
        users = Optional.of(usersRepo.findAll());
        users
            .get()
            .forEach(
                user -> {
                  user.setUserAccount(null);
                  user.setPassword(null);
                });
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
            String.format(BookWorldConstants.USERNAME_NOT_FOUND, username),
            null,
            response);
        logger.error(String.format(BookWorldConstants.USERNAME_NOT_FOUND, username));
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
        logger.error(String.format(BookWorldConstants.INVALID_PASSWORD, username));
        buildAPIResponse(
            HttpStatus.BAD_REQUEST.value(), BookWorldConstants.INVALID_PASSWORD, null, response);
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
        logger.error(String.format(BookWorldConstants.USERNAME_ALREADY_EXISTS, username));
        buildAPIResponse(
            HttpStatus.NOT_ACCEPTABLE.value(),
            String.format(BookWorldConstants.USERNAME_ALREADY_EXISTS, username),
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
        newUsersAccount.setCreatedTs(new Timestamp(System.currentTimeMillis()));
        newUsersAccount.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
        newUser.setUserAccount(newUsersAccount);
        try {
          usersRepo.save(newUser);
          user.setPassword(null);
          buildAPIResponse(HttpStatus.CREATED.value(), null, user, response);
          logger.info(String.format(BookWorldConstants.ACCOUNT_CREATED_SUCCESSFULLY, username));
        } catch (Exception e) {
          logger.error("Exception while saving new user in database");
          buildAPIResponse(
              HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not create account", user, response);
          e.printStackTrace();
        }
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
        logger.error(String.format(BookWorldConstants.USERNAME_NOT_FOUND, username));
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
          buildAPIResponse(HttpStatus.OK.value(), null, profile, response);
        } catch (Exception e) {
          logger.error("Failed to update profile for user " + username);
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

  public synchronized APIResponse createOrder(PlaceOrder order) {
    logger.info("Placing order for user " + order.getUsername());
    logger.info(order.getOrderItems() + " ");
    final APIResponse response = new APIResponse();
    try {
      String username = order.getUsername();
      Optional<Users> user = usersRepo.findById(username);
      Map<String, Book> cartItems = new HashMap<>();
      if (user.isPresent()) {
        StringBuilder errorResponse = new StringBuilder();
        order
            .getOrderItems()
            .forEach(
                item -> {
                  Optional<Book> bookInCart = bookRepo.findById(item.getBookId());
                  if (bookInCart.isPresent()) {
                    cartItems.putIfAbsent(bookInCart.get().getBookId(), bookInCart.get());
                  } else {
                    errorResponse.append(
                        "Can't deliver required quantity of Book" + bookInCart.get().getTitle());
                    errorResponse.append("\n");
                  }
                });
        if (errorResponse.length() > 0) {
          buildAPIResponse(HttpStatus.NO_CONTENT.value(), errorResponse.toString(), null, response);
          return response;
        }
        Orders newOrder = new Orders();
        newOrder.setOrderId(UUID.randomUUID().toString());
        Address address = new Address();
        address.setHouseNumber(order.getAddress().getHouseNumber());
        address.setLocality(order.getAddress().getLocality());
        address.setCity(order.getAddress().getCity());
        address.setCountry(order.getAddress().getCountry());
        address.setPincode(order.getAddress().getPincode());
        newOrder.setShippingAddress(address);
        List<OrderItem> orderItems = new ArrayList<>();
        order
            .getOrderItems()
            .forEach(
                singleOrder -> {
                  OrderItem orderItem = new OrderItem();
                  orderItem.setBookId(singleOrder.getBookId());
                  orderItem.setQuantity(singleOrder.getQuantity());
                  orderItems.add(orderItem);
                });
        newOrder.setOrders(orderItems);
        Payment payment = new Payment();
        payment.setDeliveryCharges(order.getPayment().getDeliveryCharges());
        payment.setPrice(order.getPayment().getPrice());
        payment.setPaymentStatus(
            PaymentStatus.valueOfStatus(order.getPayment().getPaymentStatus()));
        payment.setPaymentId(newOrder.getOrderId());
        payment.setPaymentMode(PaymentMode.valueOfStatus(order.getPayment().getPaymentMode()));
        newOrder.setPayment(payment);
        newOrder.setStatus(OrderStatus.valueOfStatus(order.getOrderstatus()));
        newOrder.setUserName(username);
        newOrder.setCreatedTs(new Timestamp(System.currentTimeMillis()));
        newOrder.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
        user.get().getUserAccount().getListOfOrders().add(newOrder);
        usersRepo.save(user.get());
        order.setId(newOrder.getOrderId());
        order
            .getOrderItems()
            .forEach(
                item -> {
                  Book cartItem = cartItems.getOrDefault(item.getBookId(), null);
                  cartItem.setAvailableCount(cartItem.getAvailableCount() - item.getQuantity());
                  bookRepo.save(cartItem);
                });

        buildAPIResponse(HttpStatus.CREATED.value(), null, order, response);
        logger.info("Order placed successfully for the user " + username);
      } else {
        buildAPIResponse(
            HttpStatus.NOT_FOUND.value(),
            String.format(BookWorldConstants.USERNAME_NOT_FOUND, username),
            null,
            response);
        logger.error("User does not exist");
      }
    } catch (Exception e) {
      logger.error("Exception in placing order");
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse getOrder(String id) {

    logger.info("Getting order by id" + id);
    final APIResponse response = new APIResponse();
    try {
      Optional<Orders> order = ordersRepo.findById(id);
      if (order.isPresent()) {
        buildAPIResponse(HttpStatus.OK.value(), null, order, response);
      } else {
        buildAPIResponse(
            HttpStatus.NOT_FOUND.value(), "Order with" + id + " does not exist", null, response);
      }
    } catch (Exception e) {
      logger.error("Exception in getOrder()");
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse getOrders(String username) {
    logger.info("Getting orders of user " + username);
    final APIResponse response = new APIResponse();
    try {
      Optional<List<Orders>> orders =
          Optional.ofNullable(usersAccountRepo.findById(username).get().getListOfOrders());
      buildAPIResponse(HttpStatus.OK.value(), null, orders, response);
      logger.info("Getting user orders...");
    } catch (Exception e) {
      logger.error("Exception in getOrders()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Could not fetch user's orders",
          null,
          response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse removeUser(String username) {
    logger.info("Removing the user..." + username);
    APIResponse response = new APIResponse();
    try {
      usersRepo.deleteById(username);
      logger.info("User deleted successfully");
      buildAPIResponse(
          HttpStatus.NO_CONTENT.value(),
          null,
          "user " + username + " deleted successfully",
          response);
    } catch (Exception e) {
      logger.error("Exception in removeUser()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not delete user!", null, response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse updateOrder(PlaceOrder order) {
    logger.info("Updating order..." + order.getId());
    APIResponse response = new APIResponse();
    try {
      Optional<Orders> myOrder = ordersRepo.findById(order.getId());
      if (myOrder.isPresent()) {
        myOrder.get().setStatus(OrderStatus.valueOfStatus(order.getOrderstatus()));
        ordersRepo.save(myOrder.get());
        buildAPIResponse(
            HttpStatus.ACCEPTED.value(),
            null,
            "Order Id " + order.getId() + " successfully updated!",
            response);
        logger.info("Successfully update order " + order.getId());
      } else {
        buildAPIResponse(
            HttpStatus.NOT_FOUND.value(),
            "Order Id" + order.getId() + " does not exist",
            null,
            response);
        logger.error("Could not find order " + order.getId());
      }
    } catch (Exception e) {
      logger.error("Exception in updateOrder()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not update order!", null, response);

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
