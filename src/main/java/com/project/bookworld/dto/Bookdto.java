package com.project.bookworld.dto;

import java.io.Serializable;

public class Bookdto implements Serializable {

  private static final long serialVersionUID = 1L;
  private String bookId;
  private int count;

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
