package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.Reviews;

public interface UserReviewsRepostiory extends JpaRepository<Reviews, String> {}
