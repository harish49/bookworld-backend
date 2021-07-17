package com.project.bookworld.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class Review implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private String username;
  private String bookId;
  private int rating;
  private String comment;
  private Timestamp createdAt;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

public String getId(){return id;}

public void setId(String id){this.id = id;}
}
