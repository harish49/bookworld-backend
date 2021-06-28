package com.project.bookworld.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.entities.Book;
import com.project.bookworld.entities.Users;
import com.project.bookworld.utils.BookUtils;

@RestController
@CrossOrigin
@RequestMapping(BookWorldConstants.BOOKS)
public class BookHandler {

  private static final Logger logger = LoggerFactory.getLogger(BookHandler.class);
  @Autowired private Environment env;

  @SuppressWarnings({"all"})
  @GetMapping("/getbooks/{id}")
  public List<Book> getBooksFromGoogle(@PathVariable("id") String id) {
    logger.info("Started to get books from google API");
    ResponseEntity<String> response = null;
    List<Book> books = null;
    try {
      RestTemplate restTemplate = new RestTemplate();
      String URL = env.getProperty("bookSearchUrl") + id;
      response = restTemplate.getForEntity(URL, String.class);
      books = BookUtils.parseJson(response);
    } catch (RestClientException e) {
      logger.error("Exception in getBooksFromGoogle()");
      e.printStackTrace();
    }
    return books;
  }

  @GetMapping("/defaultbooks/{id}")
  public Book getBook(@PathVariable("id") String id) {

    logger.info("Fetching the book with id " + id);
    Optional<List<Book>> books = null;
    try {
      books =
          Optional.of(
              BookUtils.getDefaultBooks()
                  .stream()
                  .filter(book -> book.getBookId().equalsIgnoreCase(id))
                  .collect(Collectors.toList()));
    } catch (Exception e) {
      logger.error("Exception in getBook()");
      e.printStackTrace();
    }

    return books.isPresent() ? books.get().get(0) : new Book();
  }

  @GetMapping("/defaultbooks")
  public List<Book> getDefaultBooks() {
    Optional<List<Book>> books = null;
    logger.info("Getting default books from database");
    try {
      Thread.sleep(2000);
      books = Optional.of(BookUtils.getDefaultBooks());
    } catch (Exception e) {
      logger.error("Exception in getDefaultBooks()");
      e.printStackTrace();
    }
    return books.isPresent() ? books.get() : new ArrayList<Book>();
  }

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
