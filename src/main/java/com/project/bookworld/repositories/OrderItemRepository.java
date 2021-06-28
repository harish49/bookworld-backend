package com.project.bookworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookworld.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {}
