package com.project.bookworld;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.project.bookworld.entities.Book;
import com.project.bookworld.entities.Users;
import com.project.bookworld.repositories.BookRepository;
import com.project.bookworld.repositories.UsersAccountRepository;
import com.project.bookworld.repositories.UsersRepository;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class BookworldApplicationTests {

  @Autowired BookRepository bookrepo;
  @Autowired UsersRepository usersrepo;
  @Autowired UsersAccountRepository userAccountrepo;
  List<Book> books = null;
  List<Users> users = null;

  @BeforeAll
  public void initialize() {
    books = bookrepo.findAll();
    users = usersrepo.findAll();
  }

  @Test
  public void TestBookPersistence() {
    books.forEach(book -> bookrepo.save(book));
    System.out.println(bookrepo.findAll().size());
    assertTrue(bookrepo.findAll().size() == 6);
  }

  @Test
  public void TestUserToUserInfo() {
    users.forEach(user -> usersrepo.save(user));
    System.out.println(usersrepo.findAll().size() == 3);
    assertTrue(usersrepo.findAll().size() == 3);
    assertTrue(usersrepo.findAll().size() == userAccountrepo.findAll().size());
  }
}
