package de.friemelay.am;

import de.friemelay.am.config.Config;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.OrderTabPane;
import de.friemelay.am.ui.OrderTreePane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 */
public class UIController {
  private static UIController instance = new UIController();
  
  private OrderTreePane treePane;
  private OrderTabPane tabPane;

  private Label statusMessage = new Label("");

  private UITaskThread uiTaskThread = new UITaskThread();
  public static UIController getInstance() {
    return instance;
  }

  public Parent init() {
    BorderPane root = new BorderPane();
    this.tabPane = new OrderTabPane();
    this.treePane = new OrderTreePane();

    SplitPane splitPane = new SplitPane();
    splitPane.getItems().addAll(treePane, tabPane);
    splitPane.setDividerPositions(0.20);
    root.setCenter(splitPane);

    final MenuBar menuBar = new MenuBar();

    Menu menu = new Menu("Verwaltung");
    MenuItem menu1 = new MenuItem("Newsletter erstellen (TODO)", ResourceLoader.getImageView("email.png"));
    menu.getItems().addAll(menu1);

    Menu help = new Menu("Hilfe");
    MenuItem info = new MenuItem("Über Friemelay");
    help.getItems().addAll(info);

    menuBar.getMenus().addAll(menu);
    root.setTop(menuBar);

    HBox footer = new HBox();
    footer.setStyle("-fx-background-color:#DDD;");
    footer.setAlignment(Pos.BASELINE_RIGHT);
    statusMessage.setPadding(new Insets(0, 5, 2, 0));
    footer.getChildren().addAll(statusMessage);
    root.setBottom(footer);

    treePane.openFirst();

    uiTaskThread.start();
    setStatusMessage("Verbunden mit " + Config.getString("db.host"));
    return root;
  }

  public void setStatusMessage(String msg) {
    statusMessage.setText(msg);
    uiTaskThread.setDirty(true);
  }

  public void openOrder(Order order) {
    tabPane.openOrder(order);
  }

  public void selectTreeNode(Order order) {
    treePane.selectOrder(order);
  }

  public Label getStatusMessage() {
    return statusMessage;
  }

  public void orderConfirmationSent(Order order) {
    setStatusMessage("Bestellbestätigung versendet");
    order.setOrderStatus(Order.ORDER_STATUS_CONFIRMED);
    treePane.updateOrderStatus(order);
    DB.save(order);
    tabPane.openOrder(order).refreshOrderStatus();
  }

  public void deliveryConfirmationSent(Order order) {
    setStatusMessage("Versandbestätigung versendet");
    order.setOrderStatus(Order.ORDER_STATUS_DELIVERED);
    treePane.updateOrderStatus(order);
    DB.save(order);
    tabPane.openOrder(order).reload();
  }

  public void cancelOrder(Order order) {
    setStatusMessage("Die Bestellung " + order + " wurde storniert");
    order.setOrderStatus(Order.ORDER_STATUS_CANCELED);
    treePane.updateOrderStatus(order);
    DB.save(order);
    tabPane.openOrder(order).reload();
  }

  public void closeTab(Order order) {
    tabPane.closeTab(order);
  }
}
