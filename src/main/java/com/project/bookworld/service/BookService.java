package com.project.bookworld.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.GoogleAPIResponse;
import com.project.bookworld.entities.Book;
import com.project.bookworld.repositories.BookRepository;
import com.project.bookworld.utils.BookUtils;

@Service
public class BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookService.class);
  @Autowired private Environment env;
  @Autowired private BookRepository bookRepository;

  public APIResponse getBooksFromGoogle(@PathVariable("id") String id) {
    logger.info("Started to get books from google API");
    Optional<GoogleAPIResponse> googleResponse = Optional.ofNullable(null);
    Optional<List<Book>> books = Optional.ofNullable(null);
    APIResponse response = new APIResponse();
    try {
      RestTemplate restTemplate = new RestTemplate();
      String URL = env.getProperty("bookSearchUrl") + id;
      googleResponse =
          Optional.ofNullable(restTemplate.getForEntity(URL, GoogleAPIResponse.class).getBody());
      books = Optional.ofNullable(BookUtils.parseJson(googleResponse.get()));
      response.setStatusCode(HttpStatus.OK.value()).setResponseData(books.get());
      logger.info("Sucessfully fetched the books from google API");
      System.out.println(googleResponse);
      return response;

    } catch (Exception e) {
      logger.error("Exception in getBooksFromGoogle()");
      response
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .setError("Could not hit google API");
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse getBook(String bookId) {
    logger.info("Fetching the book with id " + bookId);
    Optional<Book> book = Optional.ofNullable(null);
    APIResponse response = new APIResponse();
    try {
      book = bookRepository.findById(bookId);
    } catch (Exception e) {
      logger.error("Exception in getBook()");
      response
          .setError("Exception in fetching book")
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      e.printStackTrace();
    }
    if (book.isPresent()) {
      logger.info("Fetching the book " + bookId);
      response.setResponseData(book).setStatusCode(HttpStatus.OK.value());
    } else {
      logger.error("Book is not available in database " + bookId);
      response.setError("Book is not available").setStatusCode(HttpStatus.NO_CONTENT.value());
    }
    return response;
  }

  public APIResponse getDefaultBooks() {
    Optional<List<Book>> books = Optional.ofNullable(null);
    APIResponse response = new APIResponse();
    logger.info("Getting default books from database");
    try {
      books = Optional.ofNullable(bookRepository.findAll());
      bookRepository.saveAll(books.get());
      System.out.println(books.get());
    } catch (Exception e) {
      logger.error("Exception in getDefaultBooks()");
      e.printStackTrace();
      response
          .setError("Exception in fetching the books from database")
          .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    if (books.isPresent()) {
      logger.info("Getting all books from database");
      response.setStatusCode(HttpStatus.OK.value()).setResponseData(books.get());
    } else {
      logger.info("No books found in database");
      response.setStatusCode(HttpStatus.NO_CONTENT.value()).setResponseData(books);
    }
    return response;
  }
}
