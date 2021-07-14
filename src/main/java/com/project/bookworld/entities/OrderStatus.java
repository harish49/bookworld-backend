package com.project.bookworld.entities;

public enum OrderStatus {
  CART("CART"),
  SHIPPED("SHIPPED"),
  DELIVERED("DELIVERED"),
  CANCELLED("CANCELLED");

  private final String alias;

  OrderStatus(String status) {
    this.alias = status;
  }

  public static OrderStatus valueOfStatus(String status) {
    for (OrderStatus e : values()) {
      if (e.alias.equals(status)) {
        return e;
      }
    }
    return null;
  }
}
