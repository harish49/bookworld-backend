package com.project.bookworld.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.bookworld.dto.GoogleAPIResponse;
import com.project.bookworld.dto.Items;
import com.project.bookworld.entities.Book;
import com.project.bookworld.entities.BookReviews;
import com.project.bookworld.security.WebSecurityConfiguration;

@SuppressWarnings({"all"})
@Component
public class BookUtils {

  private static final Logger logger = LoggerFactory.getLogger(BookUtils.class);

  @Autowired WebSecurityConfiguration securityconfiguration;

  public static List<Book> parseJson(GoogleAPIResponse booksFromGoogle) {

    List<Book> books = null;
    try {
      books =
          booksFromGoogle
              .getItems()
              .parallelStream()
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
      ArrayList<BookReviews> reviews = new ArrayList<>();
      BookReviews review = new BookReviews();
      review.setRating(3);
      review.setComment("Good");
      reviews.add(review);
      int rating = 3;
      double price = Math.floor(Math.random() * 100 + 500);
      int count = 30;
      book = new Book();
      book.setBookId(item.getId());
      book.setTitle(item.getInfo().getTitle());
      book.setAuthor(item.getInfo().getAuthors().get(0));
      book.setDescription(item.getInfo().getDescription());
      book.setPageCount(item.getInfo().getPageCount());
      book.setThumbnail(item.getInfo().getImageLinks().getThumbnail());
      book.setReviews(reviews);
      book.setRating(rating);
      book.setCategories(
          item.getInfo().getCategories() != null
              ? item.getInfo().getCategories().get(0)
              : "Unknown");
      book.setPrice(price);
      book.setAvailableCount(count);
      return book;
    } catch (Exception e) {
      logger.error("Exception in convertingToBook()");
      e.printStackTrace();
    }
    return book;
  }
}
