package de.friemelay.am;

import de.friemelay.am.config.Config;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.model.CatalogItem;
import de.friemelay.am.model.Order;
import de.friemelay.am.ui.ModelTab;
import de.friemelay.am.ui.order.OrderTab;
import de.friemelay.am.ui.util.TransitionUtil;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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

  public void setInfoMessage(String msg) {
    mainPanel.getInfoMessage().setText(msg);
  }

  public void open(AbstractModel model) {
    if(model != null) {
      mainPanel.getTabPane().open(model);
    }
  }

  public void selectOrderTreeNode(Order order) {
    mainPanel.getOrderTreePane().selectOrder(order);
  }

  public void selectCatalogTreeNode(CatalogItem item) {
    mainPanel.getCatalogTreePane().selectCatalogItem(item);
  }

  public void orderConfirmationSent(Order order) {
    setStatusMessage("Bestellbestätigung versendet");
    order.setOrderStatus(Order.ORDER_STATUS_CONFIRMED);
    mainPanel.getOrderTreePane().updateOrderStatus(order);
    DB.save(order);
    OrderTab tab = (OrderTab) mainPanel.getTabPane().open(order);
    tab.refreshOrderStatus();
  }

  public void deliveryConfirmationSent(Order order) {
    setStatusMessage("Versandbestätigung versendet");
    order.setOrderStatus(Order.ORDER_STATUS_DELIVERED);
    mainPanel.getOrderTreePane().updateOrderStatus(order);
    DB.save(order);
    OrderTab tab = (OrderTab) mainPanel.getTabPane().open(order);
    tab.reload();
  }

  public void cancelOrder(Order order) {
    setStatusMessage("Die Bestellung " + order + " wurde storniert");
    order.setOrderStatus(Order.ORDER_STATUS_CANCELED);
    mainPanel.getOrderTreePane().updateOrderStatus(order);
    DB.save(order);
    OrderTab tab = (OrderTab) mainPanel.getTabPane().open(order);
    tab.reload();
  }

  public void closeTab(AbstractModel item) {
    mainPanel.getTabPane().closeTab(item);
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

  public void refreshCatalog() {
    this.mainPanel.getCatalogTreePane().refresh();
  }

  public void closeInvalidTabs() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        List<Tab> tabs = new ArrayList<>(mainPanel.getTabPane().getTabs());
        for(Tab tab : tabs) {
          ModelTab modelTab = (ModelTab) tab;
          if(modelTab.getModel() instanceof CatalogItem) {
            CatalogItem model = (CatalogItem) modelTab.getModel();
            if(!mainPanel.getCatalogTreePane().hasCatalogItem(model)) {
              closeTab(model);
            }
          }
        }
      }
    });
  }
}
