package com.project.bookworld.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

  @Id
  @Column(name = "payment_id")
  private String paymentId;

  @Column(name = "price")
  private double price;

  @Column(name = "delivery_charges")
  private double deliveryCharges;

  @Column(name = "payment_status")
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @Column(name = "payment_mode")
  @Enumerated(EnumType.STRING)
  private PaymentMode paymentMode;

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

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

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public PaymentMode getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(PaymentMode paymentMode) {
    this.paymentMode = paymentMode;
  }

  @Override
  public String toString() {
    return "Payment [paymentId="
        + paymentId
        + ", price="
        + price
        + ", deliveryCharges="
        + deliveryCharges
        + ", paymentStatus="
        + paymentStatus
        + ", paymentMode="
        + paymentMode
        + "]";
  }
}
