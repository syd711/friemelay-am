package de.friemelay.am.model;

/**
 * Created by Matthias on 02.09.2015.
 */
public abstract class AbstractModel {
  public final static int TYPE_ORDER = -1;
  public final static int TYPE_ADDRESS = -2;
  public final static int TYPE_CUSTUMER = -3;


  public final static int TYPE_CATEGORY = 0;
  public final static int TYPE_PRODUCT = 1;
  public final static int TYPE_VARIANT = 2;


  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public abstract String getStatusIcon();
  public abstract int getType();

}
