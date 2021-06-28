package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.Users;

public interface UsersRepository extends JpaRepository<Users, String> {}
