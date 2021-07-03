package com.project.bookworld.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.service.BookService;

@RestController
@CrossOrigin
@RequestMapping(BookWorldConstants.BOOKS)
@SuppressWarnings({"all"})
public class BookHandler {

  private static final Logger logger = LoggerFactory.getLogger(BookHandler.class);
  @Autowired private BookService bookService;

  @GetMapping("/getbooks/{id}")
  public APIResponse getBooksFromGoogle(@PathVariable("id") String id) {
    APIResponse response = null;
    try {
      response = bookService.getBooksFromGoogle(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/defaultbooks/{id}")
  public APIResponse getBook(@PathVariable("id") String id) throws Exception {
    APIResponse response = null;
    try {
      response = bookService.getBook(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @GetMapping("/defaultbooks")
  public APIResponse getDefaultBooks() {
    APIResponse response = null;
    try {
      response = bookService.getDefaultBooks();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }
}
