package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.Orders;

public interface OrdersRepository extends JpaRepository<Orders, String> {}
