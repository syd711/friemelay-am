package de.friemelay.am.model;

import javafx.beans.property.*;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Order extends AbstractModel {
  public final static int ORDER_STATUS_NEW = 0;
  public final static int ORDER_STATUS_CONFIRMED = 1;
  public final static int ORDER_STATUS_DELIVERED = 2;
  public final static int ORDER_STATUS_CANCELED = 3;


  private Date creationDate;
  private IntegerProperty orderStatus = new SimpleIntegerProperty();
  private int customerId;
  private IntegerProperty paymentType = new SimpleIntegerProperty();
  private DoubleProperty totalPrice = new SimpleDoubleProperty();
  private DoubleProperty shippingCosts = new SimpleDoubleProperty();
  private StringProperty customerComments = new SimpleStringProperty();
  private StringProperty comments = new SimpleStringProperty();
  private List<OrderItem> orderItems = new ArrayList<OrderItem>();
  private Customer customer;

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public IntegerProperty getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(int orderStatus) {
    this.orderStatus.setValue(orderStatus);
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public IntegerProperty getPaymentType() {
    return paymentType;
  }

  public String getFormattedPaymentType() {
    if(paymentType.getValue() == 0) {
      return "Vorkasse";
    }
    return "Barzahlung bei Abholung";
  }

  public void setPaymentType(int paymentType) {
    this.paymentType.setValue(paymentType);
  }

  public DoubleProperty getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice.setValue(totalPrice);
  }

  public DoubleProperty getShippingCosts() {
    return shippingCosts;
  }

  public void setShippingCosts(double shippingCosts) {
    this.shippingCosts.setValue(shippingCosts);
  }

  public StringProperty getCustomerComments() {
    return customerComments;
  }

  public void setCustomerComments(String customerComments) {
    this.customerComments.setValue(customerComments);
  }

  public StringProperty getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments.setValue(comments);
  }

  @Override
  public String toString() {
    try {
      return getCustomer().getAddress().getFirstname().get() +  " "  + getCustomer().getAddress().getLastname().get()  + " (" + getFormattedCreationDateTime() + ")";
    }
    catch (NullPointerException npe) {
      Logger.getLogger(Order.class.getName()).error("NPE in Order: " + npe.getMessage(), npe);
    }
    return "Fehlerhafter Datenbestand (ID: " + getId() + ", erstellt " + getFormattedCreationDateTime() + ")";
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
  public String getFormattedCreationDateTime() {
    return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(getCreationDate()) + " Uhr";
  }

  public String getFormattedTotalPrice() {
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(getTotalPrice().get()) + " Euro";
  }

  public String getFormattedShippingCosts() {
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(getShippingCosts().get()) + " Euro";
  }

  public String getFormattedTotalPriceWithShipping() {
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(getShippingCosts().get()+getTotalPrice().get()) + " Euro";
  }

  public String getStatusIcon() {
    switch(orderStatus.getValue()) {
      case ORDER_STATUS_NEW: {
        return "green.png";
      }
      case ORDER_STATUS_CONFIRMED: {
        return "check-green.png";
      }
      case ORDER_STATUS_DELIVERED: {
        return "check-grey.png";
      }
      case ORDER_STATUS_CANCELED: {
        return "check-grey.png";
      }
      default: {
        return "green.png";
      }
    }
  }

  public String getFormattedId() {
    String orderId = String.valueOf(getId());
    while(orderId.length() < 6) {
      orderId = "0" + orderId;
    }
    return orderId;
  }

  @Override
  public int getType() {
    return AbstractModel.TYPE_ORDER;
  }
}
