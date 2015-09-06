package de.friemelay.am.model;

import javafx.beans.property.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Product extends CatalogItem {

  private List<BufferedImage> images = new ArrayList<>();
  private List<Product> variants = new ArrayList<>();
  private IntegerProperty stock = new SimpleIntegerProperty();
  private DoubleProperty price = new SimpleDoubleProperty();

  private StringProperty variantLabel = new SimpleStringProperty();

  private StringProperty variantName = new SimpleStringProperty();
  private BooleanProperty amountProperty = new SimpleBooleanProperty(true);
  private boolean variant = false;

  public StringProperty getVariantLabel() {
    return variantLabel;
  }

  public List<BufferedImage> getImages() {
    return images;
  }

  public void setImages(List<BufferedImage> images) {
    this.images = images;
  }

  public List<Product> getVariants() {
    return variants;
  }

  public void setVariants(List<Product> variants) {
    this.variants = variants;
  }

  public IntegerProperty getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock.set(stock);
  }

  public DoubleProperty getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price.set(price);
  }

  public void setVariantLabel(String variantLabel) {
    this.variantLabel.set(variantLabel);
  }

  public StringProperty getVariantName() {
    return variantName;
  }

  public void setVariantName(String variantName) {
    this.variantName.set(variantName);
  }

  public BooleanProperty getAmount() {
    return amountProperty;
  }

  public void setAmount(boolean amountProperty) {
    this.amountProperty.set(amountProperty);
  }

  public boolean isVariant() {
    return variant;
  }

  public void setVariant(boolean variant) {
    this.variant = variant;
  }

  @Override
  public String getStatusIcon() {
    if(isVariant()) {
      return "variant.png";
    }
    return "product.png";
  }

  @Override
  public String toString() {
    if(isVariant()) {
      return getVariantName().get();
    }
    return getTitle().get();
  }

  @Override
  public int getType() {
    if(isVariant()) {
      return TYPE_VARIANT;
    }
    return TYPE_PRODUCT;
  }

  public int getAmountValue() {
    if(amountProperty.get()) {
      return 1;
    }
    return 0;
  }
}
