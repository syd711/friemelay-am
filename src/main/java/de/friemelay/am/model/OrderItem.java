package de.friemelay.am.model;

import java.text.DecimalFormat;

/**
 * 
 */
public class OrderItem {
  private int order_id;
  private int amount;
  private double price;
  private String product_id;
  private String productDescription;
  private String imageUrl;
  private String url;

  public int getOrder_id() {
    return order_id;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
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
    DecimalFormat df = new DecimalFormat("#.00");
    return df.format(getPrice()) + " Euro";
  }

  public String getFormattedTotalPrice() {
    DecimalFormat df = new DecimalFormat("#.00");
    return df.format(getAmount()*getPrice()) + " Euro";
  }
}
