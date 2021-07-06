package com.project.bookworld.dto;

import java.io.Serializable;

public class JWTResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userName;
  private String token;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
