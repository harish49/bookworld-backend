package com.project.bookworld.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.Bookdto;
import com.project.bookworld.dto.Review;
import com.project.bookworld.entities.Book;
import com.project.bookworld.service.BookService;

/**
 * @author Harish Vemula
 *     <p>Controller to handle book related operations
 */
@RestController
@RequestMapping(BookWorldConstants.BOOKS)
@SuppressWarnings({"all"})
public class BookHandler {

  private static final Logger logger = LoggerFactory.getLogger(BookHandler.class);
  @Autowired private BookService bookService;

  @GetMapping("/googlebooks/{bookname}")
  public APIResponse getBooksFromGoogle(@PathVariable("bookname") String bookname) {
    APIResponse response = null;
    try {
      response = bookService.getBooksFromGoogle(bookname);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/getbook/{bookid}")
  public APIResponse getBook(@PathVariable("bookid") String bookId) {
    APIResponse response = null;
    try {
      response = bookService.getBook(bookId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/all")
  public APIResponse getAllBooks() {
    APIResponse response = null;
    try {
      response = bookService.getAllBooks();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @PostMapping("/insertbooks")
  public APIResponse insertBooks(@RequestBody Book books) {
    APIResponse response = null;
    try {
      response = bookService.insertBooks(books);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @PostMapping("/review")
  public APIResponse writeReview(@RequestBody Review review) {
    APIResponse response = null;
    try {
      response = bookService.writeReview(review);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/reviews/{bookId}")
  public APIResponse getReviews(@PathVariable("bookId") String bookId) {
    APIResponse response = null;
    try {
      response = bookService.getAllReviews(bookId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @PutMapping("/updatebook")
  public APIResponse updateBook(@RequestBody Bookdto book) {
    APIResponse response = null;
    try {
      response = bookService.updateBookCount(book);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/ping")
  public APIResponse ping() {
    logger.info("Hitting Books resource");
    return new APIResponse().setResponseData("pong").setStatusCode(HttpStatus.OK.value());
  }
}
