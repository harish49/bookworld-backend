package com.project.bookworld.dto;

import java.io.Serializable;
import java.util.Date;

public class APIResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String error;
  private int statusCode;
  private Object responseData;
  private Date date = new Date();
  private String path;

  public String getError() {
    return error;
  }

  public APIResponse setError(String error) {
    this.error = error;
    return this;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public APIResponse setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public Object getResponseData() {
    return responseData;
  }

  public APIResponse setResponseData(Object responseData) {
    this.responseData = responseData;
    return this;
  }

  public Date getDate() {
    return date;
  }

  public String getPath() {
    return path;
  }

  public APIResponse setPath(String path) {
    this.path = path;
    return this;
  }

  @Override
  public String toString() {
    return "APIResponse [error="
        + error
        + ", statusCode="
        + statusCode
        + ", responseData="
        + responseData
        + ", date="
        + date
        + ", path="
        + path
        + "]";
  }
}
