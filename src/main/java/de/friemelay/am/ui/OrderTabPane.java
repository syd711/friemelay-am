package de.friemelay.am.ui;

import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public class OrderTabPane extends BorderPane {
  private TabPane tabPane;

  public OrderTabPane() {
    tabPane = new TabPane();
    tabPane.setRotateGraphic(false);
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    tabPane.setSide(Side.TOP);
    
    setCenter(tabPane);
  }

  public void openOrder(Order order) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      OrderTab orderTab = (OrderTab) tab;
      if(orderTab.getOrder().equals(order)) {
        tabPane.getSelectionModel().select(tab);
        return;
      }
    }

    DB.loadCustomer(order);
    DB.loadAddress(order.getCustomer());
    DB.loadBillingAddress(order.getCustomer());
    
    OrderTab tab = new OrderTab(order);
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);
  }
}
