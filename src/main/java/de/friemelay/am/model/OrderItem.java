package de.friemelay.am.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.text.DecimalFormat;

/**
 * 
 */
public class OrderItem {
  private int order_id;
  private IntegerProperty amount = new SimpleIntegerProperty();
  private double price;
  private int productId;
  private String productDescription;
  private String imageUrl;
  private String url;

  public int getOrder_id() {
    return order_id;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public IntegerProperty getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount.setValue(amount);
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  @Override
  public String toString() {
    return amount + "x " + getProductDescription();
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFormattedPrice() {
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(getPrice()) + " Euro";
  }

  public String getFormattedTotalPrice() {
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(getAmount().get()*getPrice()) + " Euro";
  }
}
