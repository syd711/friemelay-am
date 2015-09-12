package de.friemelay.am.model;

import java.util.Comparator;

/**
 * Created by Matthias on 11.09.2015.
 */
public class ModelComparator implements Comparator<AbstractModel> {
  @Override
  public int compare(AbstractModel o1, AbstractModel o2) {
    return o1.toString().toLowerCase().trim().compareTo(o2.toString().toLowerCase().trim());
  }
}
