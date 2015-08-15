package de.friemelay.am.ui;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public class OrderTabPane extends BorderPane implements ChangeListener<Tab> {
  private TabPane tabPane;

  public OrderTabPane() {
    tabPane = new TabPane();
    tabPane.setRotateGraphic(false);
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    tabPane.setSide(Side.TOP);
    tabPane.getSelectionModel().selectedItemProperty().addListener(this);

    setCenter(tabPane);
  }

  public OrderTab openOrder(Order order) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      OrderTab orderTab = (OrderTab) tab;
      if(orderTab.getOrder().equals(order)) {
        tabPane.getSelectionModel().select(tab);
        return orderTab;
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
      Order order = ((OrderTab)newValue).getOrder();
      UIController.getInstance().selectTreeNode(order);
    }
  }

}
