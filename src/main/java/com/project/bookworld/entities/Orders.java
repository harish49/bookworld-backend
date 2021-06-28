package com.project.bookworld.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Orders {

  @Id
  @Column(name = "order_id")
  private String orderId;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "shipping_address")
  @Embedded
  private Address shippingAddress;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @OneToMany
  @JoinColumn(name = "order_id_fk", referencedColumnName = "order_id")
  private List<OrderItem> orders = new ArrayList<>();

  @Column(name = "created_ts", updatable = false)
  private Timestamp createdTs;

  @Column(name = "updated_ts")
  private Timestamp updatedTs;

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public List<OrderItem> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderItem> orders) {
    this.orders = orders;
  }

  public Timestamp getCreatedTs() {
    return createdTs;
  }

  public void setCreatedTs(Timestamp createdTs) {
    this.createdTs = createdTs;
  }

  public Timestamp getUpdatedTs() {
    return updatedTs;
  }

  public void setUpdatedTs(Timestamp updatedTs) {
    this.updatedTs = updatedTs;
  }

  @Override
  public String toString() {
    return "Orders [orderId="
        + orderId
        + ", quantity="
        + quantity
        + ", shippingAddress="
        + shippingAddress
        + ", payment="
        + payment
        + ", orders="
        + orders
        + "]";
  }
}
