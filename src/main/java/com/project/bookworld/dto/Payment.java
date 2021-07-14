package com.project.bookworld.dto;

import java.io.Serializable;

public class Payment implements Serializable {

  private static final long serialVersionUID = 1L;
  private double price;
  private double deliveryCharges;
  private String paymentStatus;
  private String paymentMode;

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getDeliveryCharges() {
    return deliveryCharges;
  }

  public void setDeliveryCharges(double deliveryCharges) {
    this.deliveryCharges = deliveryCharges;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }
}
