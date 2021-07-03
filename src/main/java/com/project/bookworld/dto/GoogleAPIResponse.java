package com.project.bookworld.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleAPIResponse {

  @JsonProperty("items")
  private List<Items> items;

  public List<Items> getItems() {
    return items;
  }

  public void setItems(List<Items> items) {
    this.items = items;
  }
}
