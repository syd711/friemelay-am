package de.friemelay.am.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Category extends CatalogItem {

  private List<Category> children = new ArrayList<>();
  private List<Product> products = new ArrayList<>();
  private boolean topLevel;

  public boolean isTopLevel() {
    return topLevel;
  }

  public void setTopLevel(boolean topLevel) {
    this.topLevel = topLevel;
  }

  public List<Category> getChildren() {
    return children;
  }

  public void setChildren(List<Category> children) {
    this.children = children;
  }


  @Override
  public String getStatusIcon() {
    return "category.png";
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }


  @Override
  public int getType() {
    return TYPE_CATEGORY;
  }
}
