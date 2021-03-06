package com.project.bookworld.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reviews")
public class Reviews {

  @Id
  @Column(name = "review_id")
  private String reviewid;

  @Column(name = "book_comment")
  private String comment;

  @Column(name = "book_id")
  private String bookId;

  @Column(name = "user_name")
  private String userName;

  private int rating;

  @Column(name = "created_ts", updatable = false)
  private Timestamp createdTs;

  @Column(name = "updated_ts")
  private Timestamp updatedTs;

  public String getReviewid() {
    return reviewid;
  }

  public void setReviewid(String reviewid) {
    this.reviewid = reviewid;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
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
    return "BookReviews [reviewid="
        + reviewid
        + ", comment="
        + comment
        + ", rating="
        + rating
        + "]";
  }
}
