package de.friemelay.am.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.awt.image.BufferedImage;

/**
 *
 */
public abstract class CatalogItem extends AbstractModel {

  private int parentId;
  private StringProperty title = new SimpleStringProperty();
  private StringProperty shortDescription = new SimpleStringProperty();
  private StringProperty details = new SimpleStringProperty();
  private BufferedImage image;

  public StringProperty getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public StringProperty getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription.set(shortDescription);
  }

  public StringProperty getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details.setValue(details);
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }


  @Override
  public String toString() {
    return title.get();
  }

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  public abstract int getType();
}
