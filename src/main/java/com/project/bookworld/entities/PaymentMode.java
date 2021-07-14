package com.project.bookworld.entities;

public enum PaymentMode {
  CREDITCARD("CREDITCARD"),
  RAZORPAY("RAZORPAY");
  public final String alias;

  PaymentMode(String mode) {
    this.alias = mode;
  }

  public static PaymentMode valueOfStatus(String status) {
    for (PaymentMode e : values()) {
      if (e.alias.equals(status)) {
        return e;
      }
    }
    return null;
  }
}
