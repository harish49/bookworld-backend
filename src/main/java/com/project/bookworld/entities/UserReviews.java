package com.project.bookworld.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity(name = "user_reviews")
public class UserReviews {

  @Id
  @Column(name = "user_review")
  private String userReview;

  @Column(name = "book_comment")
  private String comment;

  @Column(name = "created_ts", updatable = false)
  private Timestamp createdTs;

  @Column(name = "updated_ts")
  private Timestamp updatedTs;

  public String getUserReview() {
    return userReview;
  }

  public void setUserReview(String userReview) {
    this.userReview = userReview;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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
    return "UserReviews [userReview=" + userReview + ", comment=" + comment + "]";
  }
}
