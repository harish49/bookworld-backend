package com.project.bookworld.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.GoogleAPIResponse;
import com.project.bookworld.dto.Items;
import com.project.bookworld.entities.Book;
import com.project.bookworld.security.WebSecurityConfiguration;

@SuppressWarnings({"all"})
@Component
public class BookUtils {

  private static final Logger logger = LoggerFactory.getLogger(BookUtils.class);

  @Autowired WebSecurityConfiguration securityconfiguration;
  @Autowired private static Environment env;

  public static List<Book> parseJson(GoogleAPIResponse booksFromGoogle) {

    List<Book> books = null;
    try {
      books =
          booksFromGoogle
              .getItems()
              .parallelStream()
              .filter(
                  book -> BookWorldConstants.DEFAULT_BOOKS.contains(book.getId()) ? false : true)
              .map(item -> convertToBook(item))
              .collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("Exception in converting Map to Book Object");
      e.printStackTrace();
    }
    return books.isEmpty() ? new ArrayList<Book>() : books;
  }

  private static Book convertToBook(Items item) {
    Book book = null;
    logger.info("Converting into Book.." + item);
    try {
      double price = Math.floor(Math.random() * 100 + 500);
      book = new Book();
      book.setBookId(item.getId());
      book.setTitle(item.getInfo().getTitle());
      book.setAuthor(item.getInfo().getAuthors().get(0));
      book.setDescription(item.getInfo().getDescription());
      book.setPageCount(item.getInfo().getPageCount());
      book.setThumbnail(item.getInfo().getImageLinks().getThumbnail());
      book.setCategories(
          item.getInfo().getCategories() != null
              ? item.getInfo().getCategories().get(0)
              : "Unknown");
      book.setPrice(price);
      book.setAvailableCount(30);
      return book;
    } catch (Exception e) {
      logger.error("Exception in convertingToBook()");
      e.printStackTrace();
    }
    return book;
  }
}
