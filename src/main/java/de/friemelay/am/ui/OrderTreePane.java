package de.friemelay.am.ui;

import de.friemelay.am.Control;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import de.friemelay.am.resources.ResourceLoader;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OrderTreePane extends BorderPane implements EventHandler<MouseEvent> {

  private final TreeItem<Object> treeRoot = new TreeItem<Object>(new Order());
  private TreeView treeView;
  private List<Order> orders = new ArrayList<Order>();

  public OrderTreePane() {
    treeView = new TreeView<Object>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(false);
    treeView.setRoot(treeRoot);

    setCenter(treeView);

    ToolBar toolbar = new ToolBar();
    Button refreshButton = new Button("", ResourceLoader.getImageView("refresh.png"));
    refreshButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        reload();
      }
    });
    //refreshButton.setTooltip(Tooltip.);
    toolbar.getItems().add(refreshButton);
    toolbar.getItems().add(new Button("", ResourceLoader.getImageView("remove.gif")));
    TextField searchField = new TextField();
    toolbar.getItems().add(searchField);
    Button searchButton = new Button("", ResourceLoader.getImageView("search.png"));
    toolbar.getItems().add(searchButton);
    setTop(toolbar);

    reload();
  }

  public void handle(MouseEvent event) {
    if(event.getClickCount() == 2)
    {
      TreeItem selectedItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
      if(selectedItem.getValue() instanceof Order) {
        TreeItem<Order> item = (TreeItem<Order>)selectedItem;
        Control.getInstance().openOrder(item.getValue());
      }
    }
  }

  private void reload() {
    treeRoot.getChildren().removeAll(treeRoot.getChildren());


    orders = DB.getOrders();
    for(Order order : orders) {
      TreeItem<Object> orderTreeItem = new TreeItem<Object>(order, ResourceLoader.getImageView("green.png"));
      List<OrderItem> orderItems = order.getOrderItems();
      for(OrderItem orderItem : orderItems) {
        TreeItem<Object> orderItemTreeItem = new TreeItem<Object>(orderItem, ResourceLoader.getImageView("item.png"));
        orderTreeItem.getChildren().add(orderItemTreeItem);
      }

      treeRoot.getChildren().add(orderTreeItem);
    }

    treeRoot.setExpanded(true);
  }

  public void openFirst() {
    ObservableList<TreeItem<Object>> children = treeRoot.getChildren();
    if(!children.isEmpty()) {
      TreeItem<Object> treeItem = children.get(0);
      Order order = (Order) treeItem.getValue();
      Control.getInstance().openOrder(order);
    }
  }

  public void selectOrder(Order order) {
    int i = orders.indexOf(order);
    treeView.getSelectionModel().select(i);
  }
}
