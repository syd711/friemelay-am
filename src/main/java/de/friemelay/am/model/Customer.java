package de.friemelay.am.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
  private StringProperty customerStatus = new SimpleStringProperty();
  private int addressId;
  private int billingAddressId;
  private StringProperty email = new SimpleStringProperty();
  private StringProperty customerPassword = new SimpleStringProperty();
  private StringProperty phone = new SimpleStringProperty();
  private IntegerProperty newsletter = new SimpleIntegerProperty();
  private StringProperty password = new SimpleStringProperty();

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

  public StringProperty getCustomerStatus() {
    return customerStatus;
  }

  public void setCustomerStatus(String customerStatus) {
    this.customerStatus.setValue(customerStatus);
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

  public StringProperty getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email.setValue(email);
  }

  public StringProperty getCustomerPassword() {
    return customerPassword;
  }

  public void setCustomerPassword(String customerPassword) {
    this.customerPassword.setValue(customerPassword);
  }

  public StringProperty getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone.setValue(phone);
  }

  public IntegerProperty getNewsletter() {
    return newsletter;
  }

  public void setNewsletter(int newsletter) {
    this.newsletter.setValue(newsletter);
  }

  public void setPassword(String password) {
    this.password.setValue(password);
  }

  public StringProperty getPassword() {
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
