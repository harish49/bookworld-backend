package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.Book;

public interface BookRepository extends JpaRepository<Book, String> {}
