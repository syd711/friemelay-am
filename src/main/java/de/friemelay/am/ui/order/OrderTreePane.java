package de.friemelay.am.ui.order;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.util.ProgressForm;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    refreshButton.setTooltip(new Tooltip("Bestellungen neu laden"));
    toolbar.getItems().add(refreshButton);

    Button trashButton = new Button("", ResourceLoader.getImageView("trash.png"));
    trashButton.setTooltip(new Tooltip("Bestellung löschen"));
    trashButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Order selection = getSelectedOrder();
        if(selection != null) {
          boolean confirmation = WidgetFactory.showConfirmation("Bestellung löschen?", "Soll die Bestellung '" + selection + "' wirklich gelöscht werden?" +
              "\nAlternativ kann die Bestellung storniert werden und bleibt so in der Bestellhistorie erhalten.");
          if(confirmation) {
            DB.delete(selection);
            UIController.getInstance().closeTab(selection);
            reload();
          }
        }
      }
    });
    toolbar.getItems().add(trashButton);

    setTop(toolbar);
  }

  public void handle(MouseEvent event) {
    if(event.getClickCount() == 2) {
      UIController.getInstance().openOrder(getSelectedOrder());
    }
  }

  public Order getSelectedOrder() {
    TreeItem selectedItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
    if(selectedItem != null && selectedItem.getValue() instanceof Order) {
      TreeItem<Order> item = (TreeItem<Order>) selectedItem;
      return item.getValue();
    }
    return null;
  }

  public void reload() {
    ProgressForm pForm = new ProgressForm(UIController.getInstance().getStage().getScene(), "Lade Bestellungen");

    // In real life this task would do something useful and return
    // some meaningful result:
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws InterruptedException {
        treeRoot.getChildren().removeAll(treeRoot.getChildren());


        orders = DB.getOrders();
        for(Order order : orders) {
          TreeItem<Object> orderTreeItem = new TreeItem<Object>(order, ResourceLoader.getImageView(order.getStatusIcon()));
//      List<OrderItem> orderItems = order.getOrderItems();
//      for(OrderItem orderItem : orderItems) {
//        TreeItem<Object> orderItemTreeItem = new TreeItem<Object>(orderItem, ResourceLoader.getImageView("item.png"));
//        orderTreeItem.getChildren().add(orderItemTreeItem);
//      }
          treeRoot.getChildren().add(orderTreeItem);
        }

        treeRoot.setExpanded(true);
        updateProgress(10, 10);
        return null;
      }
    };

    // binds progress of progress bars to progress of task:
    pForm.activateProgressBar(task);

    // in real life this method would get the result of the task
    // and update the UI based on its value:
    task.setOnSucceeded(event -> {
      pForm.getDialogStage().close();
    });

    pForm.getDialogStage().show();
    Thread thread = new Thread(task);
    thread.start();
  }

  public void openFirst() {
    ObservableList<TreeItem<Object>> children = treeRoot.getChildren();
    if(!children.isEmpty()) {
      TreeItem<Object> treeItem = children.get(0);
      Order order = (Order) treeItem.getValue();
      UIController.getInstance().openOrder(order);
    }
  }

  public void selectOrder(Order order) {
    int i = orders.indexOf(order);
    treeView.getSelectionModel().select(i);
  }

  public void updateOrderStatus(Order order) {
    ObservableList<TreeItem<Object>> children = treeRoot.getChildren();
    if(!children.isEmpty()) {
      for(TreeItem<Object> child : children) {
        Order orderChild = (Order) child.getValue();
        if(orderChild.getId() == order.getId()) {
          child.setGraphic(ResourceLoader.getImageView(order.getStatusIcon()));
          return;
        }
      }
    }
  }

}
