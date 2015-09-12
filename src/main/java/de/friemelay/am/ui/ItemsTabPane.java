package de.friemelay.am.ui;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.model.Category;
import de.friemelay.am.model.Order;
import de.friemelay.am.model.Product;
import de.friemelay.am.ui.catalog.CategoryTab;
import de.friemelay.am.ui.catalog.ProductTab;
import de.friemelay.am.ui.catalog.VariantTab;
import de.friemelay.am.ui.order.OrderTab;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public class ItemsTabPane extends BorderPane implements ChangeListener<Tab> {
  private javafx.scene.control.TabPane tabPane;

  public ItemsTabPane() {
    tabPane = new javafx.scene.control.TabPane();
    tabPane.setRotateGraphic(false);
    tabPane.setTabClosingPolicy(javafx.scene.control.TabPane.TabClosingPolicy.SELECTED_TAB);
    tabPane.setSide(Side.TOP);
    tabPane.getSelectionModel().selectedItemProperty().addListener(this);

    setCenter(tabPane);
  }

  public ModelTab open(AbstractModel model) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      ModelTab modelTab = (ModelTab) tab;
      if(modelTab.getModel().getId() == model.getId() && modelTab.getModel().getType() == model.getType()) {
        tabPane.getSelectionModel().select(tab);
        return modelTab;
      }
    }


    ModelTab tab = null;
    if(model instanceof Category) {
      tab = new CategoryTab((Category) model);
    }
    else if (model instanceof Product) {
      Product product = (Product) model;
      DB.loadImages(product);

      if(product.isVariant()) {
        tab = new VariantTab(product);
      }
      else {
        tab = new ProductTab(product);
      }
    }
    else if (model instanceof Order) {
      Order order = (Order) model;
      DB.loadCustomer(order);
      DB.loadAddress(order.getCustomer());
      DB.loadBillingAddress(order.getCustomer());
      tab = new OrderTab((Order) model);
    }
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);
    return tab;
  }


  public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
    if(newValue != null) {
      if(newValue instanceof OrderTab) {
        Order order = ((OrderTab) newValue).getModel();
        UIController.getInstance().selectOrderTreeNode(order);
      }
      else if(newValue instanceof CategoryTab) {
        Category category = ((CategoryTab) newValue).getModel();
        UIController.getInstance().selectCatalogTreeNode(category);
      }

    }
  }

  public void closeTab(AbstractModel item) {
    if(item == null) {
      Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
      tabPane.getTabs().removeAll(selectedItem);
      return;
    }

    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      ModelTab modelTab = (ModelTab) tab;
      if(modelTab.getModel().getId() == item.getId()) {
        tabPane.getTabs().removeAll(tab);
        return;
      }
    }
  }

  public ObservableList<Tab> getTabs() {
    return tabPane.getTabs();
  }
}
