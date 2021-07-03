package com.project.bookworld.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {

  @JsonProperty("title")
  private String title;

  @JsonProperty("authors")
  private List<String> authors;

  @JsonProperty("description")
  private String description;

  @JsonProperty("pageCount")
  private int pageCount;

  @JsonProperty("imageLinks")
  private ImageLinks imageLinks;

  @JsonProperty("categories")
  private List<String> categories;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getAuthors() {
    return authors;
  }

  public void setAuthors(List<String> authors) {
    this.authors = authors;
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

  public ImageLinks getImageLinks() {
    return imageLinks;
  }

  public void setImageLinks(ImageLinks imageLinks) {
    this.imageLinks = imageLinks;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }
}
