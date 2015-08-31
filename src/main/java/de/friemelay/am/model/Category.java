package de.friemelay.am.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Category extends CatalogItem {

  private List<Category> children = new ArrayList<Category>();
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

}
