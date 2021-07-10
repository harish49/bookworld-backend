package com.project.bookworld.entities;

public enum PaymentStatus {
  SUCCESS("SUCCESS"),
  FAILED("FAILED"),
  PROCESSING("FAILED"),
  ;

  public final String alias;

  PaymentStatus(String status) {
    this.alias = status;
  }

  public static PaymentStatus valueOfStatus(String status) {
    for (PaymentStatus e : values()) {
      if (e.alias.equals(status)) {
        return e;
      }
    }
    return null;
  }
}
