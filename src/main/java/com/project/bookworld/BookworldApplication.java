package com.project.bookworld;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookworldApplication {

  private static final Logger logger = LoggerFactory.getLogger(BookworldApplication.class);

  public static void main(final String... args) {
    SpringApplication.run(BookworldApplication.class, args);
  }

  @PostConstruct
  public void init() {
    logger.info("Bookworld application started");
  }

  @PreDestroy
  public void destroy() {
    logger.info("Bookworld application ended");
  }
}
