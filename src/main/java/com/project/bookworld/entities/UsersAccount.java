package com.project.bookworld.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user_account")
public class UsersAccount {

  @Id
  @Column(name = "user_name")
  private String userName;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_order", referencedColumnName = "user_name")
  private List<Orders> listOfOrders = new ArrayList<>();

  @Column(name = "created_ts", updatable = false)
  private Timestamp createdTs;

  @Column(name = "updated_ts")
  private Timestamp updatedTs;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public List<Orders> getListOfOrders() {
    return listOfOrders;
  }

  public void setListOfOrders(List<Orders> listOfOrders) {
    this.listOfOrders = listOfOrders;
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
    return "UsersAccount [userName="
        + userName
        + ", listOfOrders="
        + listOfOrders
        + ", createdTs="
        + createdTs
        + ", updatedTs="
        + updatedTs
        + "]";
  }
}
