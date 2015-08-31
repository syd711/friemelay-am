package de.friemelay.am;

import de.friemelay.am.config.Config;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import de.friemelay.am.ui.util.TransitionUtil;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 */
public class UIController {
  private static UIController instance = new UIController();

  private Stage stage;
  private MainPanel mainPanel;

  private UITaskThread uiTaskThread = new UITaskThread();

  public static UIController getInstance() {
    return instance;
  }

  public Parent init() {
    mainPanel = new MainPanel();
    uiTaskThread.start();
    setStatusMessage("Verbunden mit " + Config.getString("db.host"));
    return mainPanel;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStatusMessage(String msg) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        mainPanel.getStatusMessage().setText(msg);
        uiTaskThread.setDirty(true);
      }
    });
  }

  public void openOrder(Order order) {
    if(order != null) {
      mainPanel.getTabPane().openOrder(order);
    }
  }

  public void selectTreeNode(Order order) {
    mainPanel.getOrderTreePane().selectOrder(order);
  }

  public void orderConfirmationSent(Order order) {
    setStatusMessage("Bestellbestätigung versendet");
    order.setOrderStatus(Order.ORDER_STATUS_CONFIRMED);
    mainPanel.getOrderTreePane().updateOrderStatus(order);
    DB.save(order);
    mainPanel.getTabPane().openOrder(order).refreshOrderStatus();
  }

  public void deliveryConfirmationSent(Order order) {
    setStatusMessage("Versandbestätigung versendet");
    order.setOrderStatus(Order.ORDER_STATUS_DELIVERED);
    mainPanel.getOrderTreePane().updateOrderStatus(order);
    DB.save(order);
    mainPanel.getTabPane().openOrder(order).reload();
  }

  public void cancelOrder(Order order) {
    setStatusMessage("Die Bestellung " + order + " wurde storniert");
    order.setOrderStatus(Order.ORDER_STATUS_CANCELED);
    mainPanel.getOrderTreePane().updateOrderStatus(order);
    DB.save(order);
    mainPanel.getTabPane().openOrder(order).reload();
  }

  public void closeTab(Order order) {
    mainPanel.getTabPane().closeTab(order);
  }

  public void reloadOrders() {
    mainPanel.getOrderTreePane().reload();
  }

  public void reloadCatalog() {
    mainPanel.getCatalogTreePane().reload();
  }

  public void resetSelectedOrder() {
    Order order = mainPanel.getOrderTreePane().getSelectedOrder();
    if(order != null) {
      mainPanel.getTabPane().closeTab(order);
      order.setOrderStatus(Order.ORDER_STATUS_NEW);
      DB.save(order);
      reloadOrders();
    }
  }

  public void fadeOutStatusMessage() {
    Platform.runLater(new Runnable() {
      public void run() {
        final Label statusMessage = mainPanel.getStatusMessage();
        FadeTransition outFader = TransitionUtil.createOutFader(statusMessage, 1000);
        outFader.setOnFinished(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            UIController.getInstance().setStatusMessage("");
            statusMessage.setOpacity(1);
          }
        });
        outFader.play();
      }
    });
  }

  public void loadData() {
    reloadOrders();
    reloadCatalog();
    mainPanel.expandCatalog();
  }
}
