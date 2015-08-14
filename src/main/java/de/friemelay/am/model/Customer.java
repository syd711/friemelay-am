package de.friemelay.am.model;

import java.sql.Date;

/**
 * 
 */
public class Customer {

  private Address address;
  private Address billingAddress;
  
  private int id;
  private Date creationDate;
  private Date lastLogin;
  private String customerStatus;
  private int addressId;
  private int billingAddressId;
  private String email;
  private String customerPassword;
  private String phone;
  private int newsletter;
  private String password;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public String getCustomerStatus() {
    return customerStatus;
  }

  public void setCustomerStatus(String customerStatus) {
    this.customerStatus = customerStatus;
  }

  public int getAddressId() {
    return addressId;
  }

  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }

  public int getBillingAddressId() {
    return billingAddressId;
  }

  public void setBillingAddressId(int billingAddressId) {
    this.billingAddressId = billingAddressId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCustomerPassword() {
    return customerPassword;
  }

  public void setCustomerPassword(String customerPassword) {
    this.customerPassword = customerPassword;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public int getNewsletter() {
    return newsletter;
  }

  public void setNewsletter(int newsletter) {
    this.newsletter = newsletter;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }
}
