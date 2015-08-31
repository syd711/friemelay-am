package de.friemelay.am.model;

import java.sql.Blob;

/**
 *
 */
public class CatalogItem {

  private int id;
  private int parentId;
  private String title;
  private String titleText;
  private String description;
  private Blob image;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitleText() {
    return titleText;
  }

  public void setTitleText(String titleText) {
    this.titleText = titleText;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Blob getImage() {
    return image;
  }

  public void setImage(Blob image) {
    this.image = image;
  }


  @Override
  public String toString() {
    return title;
  }

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }


}
