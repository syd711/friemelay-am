package de.friemelay.am.ui;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.*;
import de.friemelay.am.ui.catalog.CatalogTab;
import de.friemelay.am.ui.catalog.CategoryTab;
import de.friemelay.am.ui.catalog.ProductTab;
import de.friemelay.am.ui.catalog.VariantTab;
import de.friemelay.am.ui.order.OrderTab;
import de.friemelay.am.ui.util.ProgressForm;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

  public void open(AbstractModel model) {
    ObservableList<Tab> tabs = tabPane.getTabs();
    for(Tab tab : tabs) {
      ModelTab modelTab = (ModelTab) tab;
      if(modelTab.getModel().getId() == model.getId() && modelTab.getModel().getType() == model.getType()) {
        tabPane.getSelectionModel().select(tab);
        if(tab instanceof OrderTab) {
          ((OrderTab) tab).reload();
        }
        return;
      }
    }


    ProgressForm pForm = new ProgressForm(UIController.getInstance().getStage().getScene(), "Lade '" + model + "'");

    // In real life this task would do something useful and return
    // some meaningful result:
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws InterruptedException {
        if(model instanceof Category) {
          updateMessage("Lade Kategoriebild");
          DB.loadImage((Category) model);
        }
        else if(model instanceof Product) {
          Product product = (Product) model;
          updateMessage("Lade Kategoriebild");
          DB.loadImage(product);
          updateMessage("Lade Produktbilder");
          DB.loadImages(product);
        }
        else if(model instanceof Order) {
          Order order = (Order) model;
          updateMessage("Lade Kundendaten");
          DB.loadCustomer(order);
          updateMessage("Lade Addresse");
          DB.loadAddress(order.getCustomer());
          updateMessage("Lade Rechnungaddresse");
          DB.loadBillingAddress(order.getCustomer());
        }

        updateProgress(10, 10);
        return null;
      }
    };

    // binds progress of progress bars to progress of task:
    pForm.activateProgressBar(task);

    // in real life this method would get the result of the task
    // and update the UI based on its value:
    task.setOnSucceeded(event -> {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          ModelTab tab = null;
          if(model instanceof Category) {
            tab = new CategoryTab((Category) model);
          }
          else if(model instanceof Product) {
            Product product = (Product) model;
            DB.loadImage(product);
            DB.loadImages(product);

            if(product.isVariant()) {
              tab = new VariantTab((Product) model);
            }
            else {
              tab = new ProductTab((Product) model);
            }
          }
          else if(model instanceof Order) {
            Order order = (Order) model;
            DB.loadCustomer(order);
            DB.loadAddress(order.getCustomer());
            DB.loadBillingAddress(order.getCustomer());

            tab = new OrderTab((Order) model);
          }

          tabPane.getTabs().add(tab);
          tabPane.getSelectionModel().select(tab);
          pForm.getDialogStage().close();
        }
      });

    });

    pForm.getDialogStage().show();
    Thread thread = new Thread(task);
    thread.start();
  }


  public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
    if(newValue != null) {
      if(newValue instanceof OrderTab) {
        Order order = ((OrderTab) newValue).getModel();
        UIController.getInstance().selectOrderTreeNode(order);
      }
      else if(newValue instanceof CatalogTab) {
        AbstractModel item = ((CatalogTab) newValue).getModel();
        UIController.getInstance().selectCatalogTreeNode((CatalogItem) item);
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
