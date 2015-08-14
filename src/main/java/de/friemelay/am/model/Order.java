package de.friemelay.am.model;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Order {
  private int id;
  private Date creationDate;
  private int orderStatus;
  private int customerId;
  private int paymentType;
  private double totalPrice;
  private double shippingCosts;
  private String customerComments;
  private String comments;
  private List<OrderItem> orderItems = new ArrayList<OrderItem>();
  private Customer customer;

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

  public int getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(int orderStatus) {
    this.orderStatus = orderStatus;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getPaymentType() {
    return paymentType;
  }

  public String getFormattedPaymentType() {
    if(paymentType == 0) {
      return "Vorkasse";
    }
    return "Barzahlung bei Abholung";
  }

  public void setPaymentType(int paymentType) {
    this.paymentType = paymentType;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public double getShippingCosts() {
    return shippingCosts;
  }

  public void setShippingCosts(double shippingCosts) {
    this.shippingCosts = shippingCosts;
  }

  public String getCustomerComments() {
    return customerComments;
  }

  public void setCustomerComments(String customerComments) {
    this.customerComments = customerComments;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  @Override
  public String toString() {
    if(getCreationDate() == null) {
      return "Bestellungen";
    }
    return id + " - " + getFormattedCreationDate();
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getFormattedCreationDate() {
    return new SimpleDateFormat("dd.MM.yyyy").format(getCreationDate());
  }

  public String getFormattedTotalPrice() {
    DecimalFormat df = new DecimalFormat("#.00");
    return df.format(getTotalPrice()) + " Euro";
  }

}
