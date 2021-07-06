package com.project.bookworld.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

  @Id
  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "user_password", nullable = false)
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "mobile_number")
  private String mobileNumber;

  @Column(name = "is_admin", nullable = false)
  private String isAdmin;

  @OneToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private UsersAccount userAccount;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(String isAdmin) {
    this.isAdmin = isAdmin;
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

  public UsersAccount getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(UsersAccount userAccount) {
    this.userAccount = userAccount;
  }

  @Override
  public String toString() {
    return "User [userName="
        + userName
        + ", email="
        + email
        + ", mobileNumber="
        + mobileNumber
        + ", isAdmin="
        + isAdmin
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Users other = (Users) obj;
    if (email == null) {
      if (other.email != null) return false;
    } else if (!email.equals(other.email)) return false;
    if (mobileNumber == null) {
      if (other.mobileNumber != null) return false;
    } else if (!mobileNumber.equals(other.mobileNumber)) return false;
    if (userName == null) {
      if (other.userName != null) return false;
    } else if (!userName.equals(other.userName)) return false;
    return true;
  }
}
