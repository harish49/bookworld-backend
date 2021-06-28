package com.project.bookworld.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

  private String houseNumber;
  private String locality;
  private String city;
  private int pincode;
  private String country;

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public int getPincode() {
    return pincode;
  }

  public void setPincode(int pincode) {
    this.pincode = pincode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return "Address [houseNumber="
        + houseNumber
        + ", locality="
        + locality
        + ", city="
        + city
        + ", pincode="
        + pincode
        + ", country="
        + country
        + "]";
  }
}
