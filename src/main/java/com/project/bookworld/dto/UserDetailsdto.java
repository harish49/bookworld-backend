package com.project.bookworld.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.bookworld.BookWorldConstants;
import com.project.bookworld.entities.Users;

public class UserDetailsdto implements UserDetails {

  private static final long serialVersionUID = 1L;

  private Users user;

  public UserDetailsdto(Users user) {
    this.user = user;
  }

  public UserDetailsdto() {}

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String role =
        user.getIsAdmin().equalsIgnoreCase("Y")
            ? BookWorldConstants.ADMIN
            : BookWorldConstants.USERS;

    SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(role);
    return List.of(authorities);
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }
}
