package com.project.bookworld.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Book {

  @Id
  @Column(name = "book_id")
  String bookId;

  @Column(name = "book_title")
  String title;

  @Column(name = "book_author")
  String author;

  @Column(name = "book_description", length = 5000)
  String description;

  @Column(name = "book_pagecount")
  int pageCount;

  @Column(name = "book_thumbnail")
  String thumbnail;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "book_id_fk", referencedColumnName = "book_id")
  List<BookReviews> reviews;

  @Column(name = "book_rating")
  int rating;

  @Column(name = "book_categories")
  String categories;

  @Column(name = "book_price")
  double price;

  @Column(name = "book_available_count")
  int availableCount;

  @Column(name = "created_ts", updatable = false)
  private Timestamp createdTs;

  @Column(name = "updated_ts")
  private Timestamp updatedTs;

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public List<BookReviews> getReviews() {
    return reviews;
  }

  public void setReviews(List<BookReviews> reviews) {
    this.reviews = reviews;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getCategories() {
    return categories;
  }

  public void setCategories(String categories) {
    this.categories = categories;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getAvailableCount() {
    return availableCount;
  }

  public void setAvailableCount(int availableCount) {
    this.availableCount = availableCount;
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Book other = (Book) obj;
    if (bookId == null) {
      if (other.bookId != null) return false;
    } else if (!bookId.equals(other.bookId)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "Book [bookId="
        + bookId
        + ", title="
        + title
        + ", author="
        + author
        + ", price="
        + price
        + "]";
  }
}
