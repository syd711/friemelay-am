package de.friemelay.am.ui;

import de.friemelay.am.Control;
import de.friemelay.am.db.DB;
import de.friemelay.am.images.ResourceLoader;
import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.util.List;

/**
 *
 */
public class OrderTreePane extends BorderPane implements EventHandler<MouseEvent> {

  private final TreeItem<Object> treeRoot = new TreeItem<Object>(new Order());
  private TreeView treeView;

  public OrderTreePane() {
    treeView = new TreeView<Object>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(false);
    treeView.setRoot(treeRoot);

    setCenter(treeView);

    ToolBar toolbar = new ToolBar();
    Button refreshButton = new Button("", ResourceLoader.getImageView("refresh.png"));
    //refreshButton.setTooltip(Tooltip.);
    toolbar.getItems().add(refreshButton);
    toolbar.getItems().add(new Button("", ResourceLoader.getImageView("delete.png")));
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
    treeRoot.getChildren().removeAll();


    List<Order> orders = DB.getOrders();
    for(Order order : orders) {
      TreeItem<Object> orderTreeItem = new TreeItem<Object>(order, ResourceLoader.getImageView("green.png"));
      List<OrderItem> orderItems = order.getOrderItems();
      for(OrderItem orderItem : orderItems) {
        TreeItem<Object> orderItemTreeItem = new TreeItem<Object>(orderItem, ResourceLoader.getImageView("green.png"));
        orderTreeItem.getChildren().add(orderItemTreeItem);
      }

      treeRoot.getChildren().add(orderTreeItem);
    }

    treeRoot.setExpanded(true);
  }
}
