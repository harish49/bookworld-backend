package com.project.bookworld.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {

  @JsonProperty("id")
  private String id;

  @JsonProperty("volumeInfo")
  private VolumeInfo info;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public VolumeInfo getInfo() {
    return info;
  }

  public void setInfo(VolumeInfo info) {
    this.info = info;
  }
}
