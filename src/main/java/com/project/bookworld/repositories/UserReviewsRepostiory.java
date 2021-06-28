package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.UserReviews;

public interface UserReviewsRepostiory extends JpaRepository<UserReviews, String> {}
