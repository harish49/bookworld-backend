package com.project.bookworld.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, String> {

  default List<Reviews> getReviewsByBookId(String bookId) {

    return findAll()
        .stream()
        .filter(review -> review.getBookId().equals(bookId))
        .collect(Collectors.toList());
  }

  List<Reviews> findByUserName(String username);
}
