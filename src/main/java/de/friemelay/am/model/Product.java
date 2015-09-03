package de.friemelay.am.model;

import javafx.beans.property.*;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Product extends CatalogItem {

  private List<Blob> images = new ArrayList<Blob>();
  private IntegerProperty stock = new SimpleIntegerProperty();
  private DoubleProperty price = new SimpleDoubleProperty();
  private StringProperty variantLabel = new SimpleStringProperty();
  private StringProperty variantName = new SimpleStringProperty();
  private BooleanProperty amountProperty = new SimpleBooleanProperty();
  private boolean variant = false;

  private List<Product> variants = new ArrayList<>();

  public List<Blob> getImages() {
    return images;
  }

  public void setImages(List<Blob> images) {
    this.images = images;
  }

  public boolean getAmountProperty() {
    return amountProperty.get();
  }

  public BooleanProperty amountPropertyProperty() {
    return amountProperty;
  }

  public void setAmountProperty(boolean amountProperty) {
    this.amountProperty.set(amountProperty);
  }

  public String getVariantName() {
    return variantName.get();
  }

  public StringProperty variantNameProperty() {
    return variantName;
  }

  public void setVariantName(String variantName) {
    this.variantName.set(variantName);
  }

  public String getVariantLabel() {
    return variantLabel.get();
  }

  public StringProperty variantLabelProperty() {
    return variantLabel;
  }

  public void setVariantLabel(String variantLabel) {
    this.variantLabel.set(variantLabel);
  }

  public double getPrice() {
    return price.get();
  }

  public DoubleProperty priceProperty() {
    return price;
  }

  public void setPrice(double price) {
    this.price.set(price);
  }

  public int getStock() {
    return stock.get();
  }

  public IntegerProperty stockProperty() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock.set(stock);
  }

  public boolean isVariant() {
    return variant;
  }

  public void setVariant(boolean variant) {
    this.variant = variant;
  }

  public List<Product> getVariants() {
    return variants;
  }

  public void setVariants(List<Product> variants) {
    this.variants = variants;
  }
}
