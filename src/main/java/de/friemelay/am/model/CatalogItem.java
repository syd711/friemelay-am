package de.friemelay.am.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Blob;

/**
 *
 */
public abstract class CatalogItem extends AbstractModel {

  private int parentId;
  private StringProperty title = new SimpleStringProperty();
  private StringProperty titleText = new SimpleStringProperty();
  private StringProperty description = new SimpleStringProperty();
  private Blob image;

  public StringProperty getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public StringProperty getTitleText() {
    return titleText;
  }

  public void setTitleText(String titleText) {
    this.titleText.set(titleText);
  }

  public StringProperty getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description.setValue(description);
  }

  public Blob getImage() {
    return image;
  }

  public void setImage(Blob image) {
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

}
