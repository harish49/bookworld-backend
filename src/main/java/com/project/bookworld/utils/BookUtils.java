package com.project.bookworld.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookworld.entities.Book;
import com.project.bookworld.entities.BookReviews;
import com.project.bookworld.entities.Users;
import com.project.bookworld.entities.UsersAccount;
import com.project.bookworld.security.WebSecurityConfiguration;

@SuppressWarnings({"all"})
@Component
public class BookUtils {

  private static final Logger logger = LoggerFactory.getLogger(BookUtils.class);

  @Autowired WebSecurityConfiguration securityconfiguration;

  public static List<Book> parseJson(ResponseEntity<String> listOfJsonBooks) {
    TypeReference<HashMap<String, Object>> typeRef =
        new TypeReference<HashMap<String, Object>>() {};
    ObjectMapper objectMapper = new ObjectMapper();
    HashMap<String, Object> map = null;
    try {
      map = objectMapper.readValue(listOfJsonBooks.getBody(), typeRef);
    } catch (JsonMappingException e) {
      logger.error("Exception in mapping JSON to Map");
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      logger.error("Exception in processing JSON to Map");
      e.printStackTrace();
    }

    List<Book> result = null;
    try {

      List<Book> books = new ArrayList<>();
      ArrayList items = (ArrayList) map.get("items");
      List<CompletableFuture<Book>> futures =
          (List<CompletableFuture<Book>>)
              items
                  .stream()
                  .map(
                      book -> {
                        return CompletableFuture.supplyAsync(
                            new Supplier<Book>() {
                              @Override
                              public Book get() {
                                final Book currBook = convertToBook(book);
                                return currBook;
                              }
                            });
                      })
                  .collect(Collectors.toList());
      result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("Exception in converting Map to Book Object");
      e.printStackTrace();
    }
    return result.isEmpty() ? new ArrayList<Book>() : result;
  }

  private static Book convertToBook(Object item) {
    Book book = null;
    logger.info("Converting into Book.." + item);
    try {
      HashMap<String, Object> itemInfo = (HashMap<String, Object>) item;
      String bookId = (String) itemInfo.get("id");
      HashMap<String, Object> volumeInfo =
          (HashMap<String, Object>) itemInfo.getOrDefault("volumeInfo", new Object());
      String title = (String) volumeInfo.getOrDefault("title", "UNKNOWN");
      ArrayList<String> authors =
          (ArrayList<String>) volumeInfo.getOrDefault("authors", new ArrayList<String>());
      String description = (String) volumeInfo.getOrDefault("description", "Not available");
      int pageCount = (int) volumeInfo.getOrDefault("pageCount", 0);
      ArrayList<String> categories =
          (ArrayList<String>) volumeInfo.getOrDefault("categories", new ArrayList<String>());
      String category = categories.size() > 0 ? categories.get(0) : "UNKNOWN";
      HashMap<String, String> imageLinks =
          (HashMap<String, String>)
              volumeInfo.getOrDefault("imageLinks", new HashMap<String, String>());
      String imageLink = imageLinks.size() > 0 ? imageLinks.get("thumbnail") : "UNKNOWN";
      ArrayList<BookReviews> reviews = new ArrayList<>();
      BookReviews review = new BookReviews();
      review.setRating(3);
      review.setComment("Good");
      reviews.add(review);
      int rating = (int) (Math.floor(Math.random() * 5) + 1);
      double price = Math.floor(Math.random() * 100 + 500);
      int count = (int) (Math.floor(Math.random() * 100) + 10);
      book = new Book();
      book.setBookId(bookId);
      book.setTitle(title);
      book.setAuthor(authors.get(0));
      book.setDescription(description);
      book.setPageCount(pageCount);
      book.setThumbnail(imageLink);
      book.setReviews(reviews);
      book.setRating(rating);
      book.setCategories(category);
      book.setPrice(price);
      book.setAvailableCount(count);
      return book;
    } catch (Exception e) {
      logger.error("Exception in convertingToBook()");
      e.printStackTrace();
    }
    return book;
  }

  public static List<Book> getDefaultBooks() {

    try {
      int rating = (int) (Math.floor(Math.random() * 5) + 1);
      double price = Math.floor(Math.random() * 100 + 500);
      int count = (int) (Math.floor(Math.random() * 100) + 10);
      ArrayList<BookReviews> reviews = new ArrayList<>();
      BookReviews review = new BookReviews();
      review.setRating(3);
      review.setComment("Good");
      reviews.add(review);

      Book book1 = new Book();
      book1.setBookId("ptiYBAAAQBAJ");
      book1.setTitle("JavaScript & jQuery: The Missing Manual");
      book1.setAuthor("David Sawyer McFarland");
      book1.setDescription(
          "JavaScript lets you supercharge your HTML with animation, interactivity, and visual effects—but many web designers find the language hard to learn. This easy-to-read guide not only covers JavaScript basics, but also shows you how to save time and effort with the jQuery and jQuery UI libraries of prewritten JavaScript code. You’ll build web pages that feel and act like desktop programs—with little or no programming. The important stuff you need to know: Pull back the curtain on JavaScript. Learn how to build a basic program with this language. Get up to speed on jQuery. Quickly assemble JavaScript programs that work well on multiple web browsers. Transform your user interface. Learn jQuery UI, the JavaScript library for interface features like design themes and controls. Make your pages interactive. Create JavaScript events that react to visitor actions. Use animations and effects. Build drop-down navigation menus, pop-ups, automated slideshows, and more. Collect data with web forms. Create easy-to-use forms that ensure more accurate visitor responses. Practice with living examples. Get step-by-step tutorials for web projects you can build yourself.");
      book1.setPageCount(686);
      book1.setCategories("Computers");
      book1.setThumbnail(
          "https://images-na.ssl-images-amazon.com/images/I/51qPrqD4H6L._SX381_BO1,204,203,200_.jpg");
      book1.setReviews(reviews);
      book1.setRating(rating);
      book1.setPrice(price);
      book1.setAvailableCount(count);

      Book book2 = new Book();
      book2.setBookId("NLngYyWFl_YC");
      book2.setTitle("Introduction To Algorithms");
      book2.setAuthor("Thomas H.. Cormen");
      book2.setDescription(
          "An extensively revised edition of a mathematically rigorous yet accessible introduction to algorithms.");
      book2.setPageCount(1180);
      book2.setCategories("Computers");
      book2.setThumbnail(
          "https://images-na.ssl-images-amazon.com/images/I/513P8XoCAEL._SX376_BO1,204,203,200_.jpg");
      book2.setReviews(reviews);
      book2.setRating(rating);
      book2.setPrice(price);
      book2.setAvailableCount(count);

      Book book3 = new Book();
      book3.setBookId("BREwDwAAQBAJ");
      book3.setTitle("Effective Java");
      book3.setAuthor("Joshua Bloch");
      book3.setDescription(
          "This highly anticipated new edition of the classic, Jolt Award-winning work has been thoroughly updated to cover Java SE 5 and Java SE 6 features introduced since the first edition. Bloch explores new design patterns and language idioms, showing you how to make the most of features ranging from generics to enums, annotations to autoboxing.");
      book3.setPageCount(264);
      book3.setCategories("Programming language");
      book3.setThumbnail("https://m.media-amazon.com/images/I/51v0xbRomJL.jpg");
      book3.setReviews(reviews);
      book3.setRating(rating);
      book3.setPrice(price);
      book3.setAvailableCount(count);

      Book book4 = new Book();
      book4.setBookId("59x0PgAACAAJ");
      book4.setTitle("The Theory Of Everything");
      book4.setAuthor("Stephen Hawking");
      book4.setDescription(
          "Collector s Edition with Audiobook read by the AuthorStephen Hawking is widely believed to be one of the world s greatest minds: a brilliant theoretical physicist whose work helped to reconfigure models of the universe and to redefine what s in it. Imagine sitting in a room listening to Hawking discuss these achievements and place them in historical context. It would be like hearing Christopher Columbus on the New World.Hawking presents a series of seven lec-tures covering everything from big bang to black holes to string theory that capture not only the brilliance of Hawking s mind but his characteristic wit as well. Of his research on black holes, which absorbed him for more than a decade, he says, It might seem a bit like looking for a black cat in a coal cellar. Hawking begins with a history of ideas about the universe, from Aristotle s determination that the Earth is round to Hubble s discovery, over 2000 years later, that the universe is expanding. Using that as a launching pad, he explores the reaches of modern physics, including theories on the origin of the universe (e.g., the big bang), the nature of black holes, and space-time.");
      book4.setPageCount(140);
      book4.setCategories("Astrophysics");
      book4.setThumbnail(
          "https://images-na.ssl-images-amazon.com/images/I/51oHUvYzbsL._SX327_BO1,204,203,200_.jpg");
      book4.setReviews(reviews);
      book4.setRating(rating);
      book4.setPrice(price);
      book4.setAvailableCount(count);

      Book book5 = new Book();
      book5.setBookId("OBGEDgAAQBAJ");
      book5.setTitle("Leonardo Da Vinci");
      book5.setAuthor("Walter Isaacson");
      book5.setDescription(
          "To read this magnificent biography of Leonardo da Vinci is to take a tour through the life and works of one of the most extraordinary human beings of all time in the company of the most engaging, informed, and insightful guide imaginable. Walter Isaacson is at once a true scholar and a spellbinding writer. And what a wealth of lessons there are to be learned in these pages.' David McCullough Based on thousands of pages from Leonardo’s astonishing notebooks and new discoveries about his life and work, Walter Isaacson weaves a narrative that connects his art to his science. He shows how Leonardo’s genius was based on skills we can improve in ourselves, such as passionate curiosity, careful observation, and an imagination so playful that it flirted with fantasy. He produced the two most famous paintings in history, The Last Supper and the Mona Lisa. But in his own mind, he was just as much a man of science and technology. With a passion that sometimes became obsessive, he pursued innovative studies of anatomy, fossils, birds, the heart, flying machines, botany, geology, and weaponry. His ability to stand at the crossroads of the humanities and the sciences, made iconic by his drawing of Vitruvian Man, made him history’s most creative genius. His creativity, like that of other great innovators, came from having wide-ranging passions. He peeled flesh off the faces of cadavers, drew the muscles that move the lips, and then painted history’s most memorable smile. He explored the math of optics, showed how light rays strike the cornea, and produced illusions of changing perspectives in The Last Supper.Isaacson also describes how Leonardo’s lifelong enthusiasm for staging theatrical productions informed his paintings and inventions. Leonardo’s delight at combining diverse passions remains the ultimate recipe for creativity. So, too, does his ease at being a bit of a misfit: illegitimate, gay, vegetarian, left-handed, easily distracted, and at times heretical. His life should remind us of the importance of instilling, both in ourselves and our children, not just received knowledge but a willingness to question it—to be imaginative and, like talented misfits and rebels in any era, to think different.");
      book5.setPageCount(576);
      book5.setCategories("Biography & Autobiography");
      book5.setThumbnail(
          "https://images-na.ssl-images-amazon.com/images/I/51SGmoOgCCL._SX322_BO1,204,203,200_.jpg");
      book5.setReviews(reviews);
      book5.setRating(rating);
      book5.setPrice(price);
      book5.setAvailableCount(count);

      Book book6 = new Book();
      book6.setBookId("K4qv1D-LKhoC");
      book6.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
      book6.setAuthor("Erich Gamma");
      book6.setDescription(
          "Capturing a wealth of experience about the design of object-oriented software, four top-notch designers present a catalog of simple and succinct solutions to commonly occurring design problems. Previously undocumented, these 23 patterns allow designers to create more flexible, elegant, and ultimately reusable designs without having to rediscover the design solutions themselves.");
      book6.setPageCount(395);
      book6.setCategories("Software Development");
      book6.setThumbnail("https://learningactors.com/wp-content/uploads/2017/10/Gang_of_four.jpg");
      book6.setReviews(reviews);
      book6.setRating(rating);
      book6.setPrice(price);
      book6.setAvailableCount(count);

      List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6);
      for (Book book : books) {
        book.setCreatedTs(new Timestamp(System.currentTimeMillis()));
        book.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
      }
      return books;
    } catch (Exception e) { // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public List<Users> getTestUsers() {

    try {
      PasswordEncoder encoder = securityconfiguration.getEncoder();
      UsersAccount useraccount = new UsersAccount();
      useraccount.setListOfOrders(new ArrayList<>());
      useraccount.setListOfReviews(new ArrayList<>());
      Users admin = new Users();
      admin.setUserName("admin");
      admin.setEmail("admin@domain.com");
      admin.setIsAdmin("Y");
      admin.setMobileNumber("12345");
      admin.setPassword(encoder.encode("pswrd_admin"));
      admin.setCreatedTs(new Timestamp(System.currentTimeMillis()));
      admin.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
      admin.setUserAccount(useraccount);
      useraccount.setUserName(admin.getUserName());

      UsersAccount useraccount1 = new UsersAccount();
      useraccount.setListOfOrders(new ArrayList<>());
      useraccount.setListOfReviews(new ArrayList<>());

      Users testuser1 = new Users();
      testuser1.setUserName("test_user1");
      testuser1.setEmail("testuser1@domain.com");
      testuser1.setIsAdmin("N");
      testuser1.setMobileNumber("123456");
      testuser1.setPassword(encoder.encode("pswrd_testuser1"));
      testuser1.setCreatedTs(new Timestamp(System.currentTimeMillis()));
      testuser1.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
      testuser1.setUserAccount(useraccount1);
      useraccount1.setUserName(testuser1.getUserName());

      UsersAccount useraccount2 = new UsersAccount();
      useraccount.setListOfOrders(new ArrayList<>());
      useraccount.setListOfReviews(new ArrayList<>());

      Users testuser2 = new Users();
      testuser2.setUserName("test_user2");
      testuser2.setEmail("testuser2@domain.com");
      testuser2.setIsAdmin("N");
      testuser2.setMobileNumber("1234567");
      testuser2.setPassword(encoder.encode("pswrd_testuser2"));
      testuser2.setCreatedTs(new Timestamp(System.currentTimeMillis()));
      testuser2.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
      testuser2.setUserAccount(useraccount2);
      useraccount2.setUserName(testuser2.getUserName());

      return Arrays.asList(admin, testuser1, testuser2);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
