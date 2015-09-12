package de.friemelay.am.ui.catalog;

import de.friemelay.am.model.CatalogItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthias on 11.09.2015.
 */
public class CatalogTreeExpandListener implements ChangeListener<Boolean> {
  private Map<String,Boolean> nodeExpandState = new HashMap<>();

  public boolean getExpandState(CatalogItem model) {
    if(!nodeExpandState.containsKey(model.toString())) {
      nodeExpandState.put(model.toString(), true);
    }

    return nodeExpandState.get(model.toString());
  }

  @Override
  public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    BooleanProperty property = (BooleanProperty) observable;
    TreeItem<CatalogItem> treeItem = (TreeItem<CatalogItem>) property.getBean();
    nodeExpandState.put(treeItem.getValue().toString(), newValue);
  }
}
