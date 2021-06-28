package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.BookReviews;

public interface BookReviewsRepository extends JpaRepository<BookReviews, String> {}
