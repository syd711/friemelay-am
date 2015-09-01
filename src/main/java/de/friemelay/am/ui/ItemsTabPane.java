package de.friemelay.am.ui;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Category;
import de.friemelay.am.model.Order;
import de.friemelay.am.ui.catalog.CategoryTab;
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

  public CategoryTab openCategory(Category category) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      if(tab instanceof CategoryTab) {
        CategoryTab categoryTab = (CategoryTab) tab;
        if(categoryTab.getCategory().equals(category)) {
          tabPane.getSelectionModel().select(tab);
          return categoryTab;
        }
      }
    }


    CategoryTab tab = new CategoryTab(category);
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);
    return tab;
  }

  public OrderTab openOrder(Order order) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      if(tab instanceof OrderTab) {
        OrderTab orderTab = (OrderTab) tab;
        if(orderTab.getOrder().equals(order)) {
          tabPane.getSelectionModel().select(tab);
          return orderTab;
        }
      }
    }

    DB.loadCustomer(order);
    DB.loadAddress(order.getCustomer());
    DB.loadBillingAddress(order.getCustomer());

    OrderTab tab = new OrderTab(order);
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);
    return tab;
  }


  public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
    if(newValue != null) {
      if(newValue instanceof OrderTab) {
        Order order = ((OrderTab) newValue).getOrder();
        UIController.getInstance().selectOrderTreeNode(order);
      }
      else if(newValue instanceof CategoryTab) {
        Category category = ((CategoryTab) newValue).getCategory();
        UIController.getInstance().selectCatalogTreeNode(category);
      }

    }
  }

  public void closeTab(Order order) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      OrderTab orderTab = (OrderTab) tab;
      if(orderTab.getOrder().getId() == order.getId()) {
        tabPane.getTabs().removeAll(orderTab);
        return;
      }
    }
  }
}
