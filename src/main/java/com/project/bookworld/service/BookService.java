package com.project.bookworld.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.dto.APIResponse;
import com.project.bookworld.dto.Bookdto;
import com.project.bookworld.dto.GoogleAPIResponse;
import com.project.bookworld.dto.Review;
import com.project.bookworld.entities.Book;
import com.project.bookworld.entities.Reviews;
import com.project.bookworld.repositories.BookRepository;
import com.project.bookworld.repositories.ReviewsRepository;
import com.project.bookworld.utils.BookUtils;

@Service
public class BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookService.class);
  public static final Map<String, Book> mapOfExistingBooks = new ConcurrentHashMap<>();
  @Autowired private Environment env;
  @Autowired private BookRepository bookRepository;
  @Autowired private ReviewsRepository reviewRepo;

  public APIResponse getBooksFromGoogle(@PathVariable("id") String id) {
    logger.info("Started to get books from google API");
    Optional<GoogleAPIResponse> googleResponse = Optional.ofNullable(null);
    Optional<List<Book>> books = Optional.ofNullable(null);
    APIResponse response = new APIResponse();
    try {
      RestTemplate restTemplate = new RestTemplate();
      String URL = env.getProperty(BookWorldConstants.BOOK_SEARCH_URL) + id;
      googleResponse =
          Optional.ofNullable(restTemplate.getForEntity(URL, GoogleAPIResponse.class).getBody());
      books = Optional.ofNullable(BookUtils.parseJson(googleResponse.get()));
      List<Book> resultantBooks = new ArrayList<>();
      if (books.isPresent() && books.get().size() > 0) {
        logger.info("Sucessfully fetched the books from google API");
        books
            .get()
            .forEach(
                book -> {
                  if (!mapOfExistingBooks.containsKey(book.getBookId())) {
                    bookRepository.save(book);
                    mapOfExistingBooks.put(book.getBookId(), book);
                    resultantBooks.add(book);
                  } else {
                    resultantBooks.add(mapOfExistingBooks.get(book.getBookId()));
                  }
                });
        buildAPIResponse(HttpStatus.OK.value(), null, books, response);

      } else {
        buildAPIResponse(
            HttpStatus.NOT_FOUND.value(), "Your search did not match any books", null, response);
        logger.info("No search items found");
      }
    } catch (Exception e) {
      logger.error("Exception in getBooksFromGoogle()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Exception in searching for book",
          null,
          response);
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

  public APIResponse writeReview(Review review) {
    logger.info("Inserting " + review.getUsername() + "'s review");
    APIResponse response = new APIResponse();
    try {
      Reviews newReview = new Reviews();
      newReview.setReviewid(UUID.randomUUID().toString());
      newReview.setBookId(review.getBookId());
      newReview.setComment(review.getComment());
      newReview.setRating(review.getRating());
      newReview.setUserName(review.getUsername());
      newReview.setCreatedTs(new Timestamp(System.currentTimeMillis()));
      newReview.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
      reviewRepo.save(newReview);
      review.setCreatedAt(newReview.getCreatedTs());
      review.setId(newReview.getReviewid());
      Optional<Book> bookToUpdate = bookRepository.findById(review.getBookId());
      bookToUpdate.get().setReviews(bookToUpdate.get().getReviews() + 1);
      bookToUpdate
          .get()
          .setRating(
              (bookToUpdate.get().getRating() + review.getRating())
                  / (bookToUpdate.get().getReviews()));
      bookRepository.save(bookToUpdate.get());
      mapOfExistingBooks.put(bookToUpdate.get().getBookId(), bookToUpdate.get());
      buildAPIResponse(HttpStatus.CREATED.value(), null, review, response);
    } catch (Exception e) {
      logger.error("Exception in writeReview()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not write a review!", null, response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse insertBooks(Book book) {
    try {
      book.setCreatedTs(new Timestamp(System.currentTimeMillis()));
      book.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
      bookRepository.save(book);
    } catch (Exception e) {
      logger.error("Exception in insertBooks");
      e.printStackTrace();
    }
    return null;
  }

  public synchronized APIResponse updateBookCount(Bookdto book) {
    logger.info("Updating book count for book " + book.getBookId());
    APIResponse response = new APIResponse();
    try {
      Optional<Book> bookTobeUpdated = bookRepository.findById(book.getBookId());
      if (bookTobeUpdated.isPresent()) {
        bookTobeUpdated.get().setAvailableCount(book.getCount());
        bookRepository.save(bookTobeUpdated.get());
        mapOfExistingBooks.put(bookTobeUpdated.get().getBookId(), bookTobeUpdated.get());
        buildAPIResponse(HttpStatus.ACCEPTED.value(), null, book, response);
        logger.info("Successfully updated book " + book.getBookId());
      } else {
        logger.error("Book is not available" + book.getBookId());
        buildAPIResponse(HttpStatus.NOT_FOUND.value(), "Book not available", null, response);
      }
    } catch (Exception e) {
      logger.error("Exception in updateBook()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not update book", null, response);
      e.printStackTrace();
    }
    return response;
  }

  public APIResponse getAllReviews(String bookId) {
    logger.info("Getting all reviews");
    APIResponse response = new APIResponse();
    try {
      List<Reviews> reviews = reviewRepo.getReviewsByBookId(bookId);
      Comparator<Reviews> sortByTimestamp =
          (reviewA, reviewB) -> {
            System.out.println(reviewA.getUpdatedTs() + " " + reviewB.getUpdatedTs());
            return reviewA.getUpdatedTs().equals(reviewB.getUpdatedTs())
                ? 0
                : reviewA.getUpdatedTs().before(reviewB.getUpdatedTs()) ? -1 : 1;
          };
      Collections.sort(reviews, sortByTimestamp);
      buildAPIResponse(HttpStatus.OK.value(), null, reviews, response);
    } catch (Exception e) {
      logger.error("Exception in getAllReviews()");
      buildAPIResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Could not fetch reviews of book",
          null,
          response);
      e.printStackTrace();
    }
    return response;
  }

  @Async
  @EventListener(ApplicationReadyEvent.class)
  private void loadExisitngBooks() {
    logger.info("Loading existing books from database");
    try {
      bookRepository
          .findAll()
          .forEach(
              book -> {
                mapOfExistingBooks.putIfAbsent(book.getBookId(), book);
              });
      logger.info("Successfully loaded books from database into temporary storage");
      mapOfExistingBooks
          .entrySet()
          .forEach(
              book -> {
                logger.info(book.getKey() + " " + book.getValue().toString());
              });
    } catch (Exception e) {
      logger.error("Exception in loading Existing books");
      e.printStackTrace();
    }
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
