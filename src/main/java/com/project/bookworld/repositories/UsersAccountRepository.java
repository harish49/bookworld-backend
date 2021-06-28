package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.UsersAccount;

public interface UsersAccountRepository extends JpaRepository<UsersAccount, String> {}
