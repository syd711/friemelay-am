package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.CatalogItem;
import de.friemelay.am.model.Category;
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
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CatalogTreePane extends BorderPane implements EventHandler<MouseEvent> {

  private final TreeItem<Object> treeRoot = new TreeItem<Object>(new Category());
  private TreeView treeView;
  private List<Category> items = new ArrayList<>();

  public CatalogTreePane() {
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
    refreshButton.setTooltip(new Tooltip("Katalog neu laden"));
    toolbar.getItems().add(refreshButton);

    Button addCategoryButton = new Button("", ResourceLoader.getImageView("new.png"));
    addCategoryButton.setTooltip(new Tooltip("Neue Kategorie erstellen"));
    addCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Category selection = getSelectedCategory();
        String name = WidgetFactory.showInputDialog("", "Kategorie anlegen", "Name der Kategorie");
        if(!StringUtils.isEmpty(name)) {
          DB.createCategory(name, selection);
          reload();
        }
      }
    });

    Button addProductButton = new Button("", ResourceLoader.getImageView("product.png"));
    addProductButton.setTooltip(new Tooltip("Neues Produkt erstellen"));
    addProductButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Category selection = getSelectedCategory();
        if(selection != null) {
          String name = WidgetFactory.showInputDialog("", "Produkt anlegen", "Name des Produktes");
          if(!StringUtils.isEmpty(name)) {
            DB.createCategory(name, selection);
            reload();
          }
        }
      }
    });

    toolbar.getItems().addAll(addCategoryButton, addProductButton);

    setTop(toolbar);
  }

  public void handle(MouseEvent event) {
    if(event.getClickCount() == 2) {
      UIController.getInstance().openCategory(getSelectedCategory());
    }
  }

  public Category getSelectedCategory() {
    TreeItem selectedItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
    if(selectedItem != null && selectedItem.getValue() instanceof Category) {
      TreeItem<Category> item = (TreeItem<Category>) selectedItem;
      return item.getValue();
    }
    return null;
  }

  public void reload() {
    ProgressForm pForm = new ProgressForm(UIController.getInstance().getStage().getScene(), "Lade Katalog");

    // In real life this task would do something useful and return
    // some meaningful result:
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws InterruptedException {
        treeRoot.getChildren().removeAll(treeRoot.getChildren());
        items = DB.loadCatalog();

        for(Category item : items) {
          TreeItem<Object> categoryTreeItem = new TreeItem<Object>(item, ResourceLoader.getImageView("category.png"));
          categoryTreeItem.setExpanded(true);
          treeRoot.getChildren().add(categoryTreeItem);
          buildTree(item.getChildren(), categoryTreeItem);
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

  private void buildTree(List<Category> children, TreeItem<Object> parent) {
    for(Category item : children) {
      TreeItem<Object> orderTreeItem = new TreeItem<Object>(item, ResourceLoader.getImageView("category.png"));
      orderTreeItem.setExpanded(true);
      parent.getChildren().add(orderTreeItem);
      buildTree(item.getChildren(), orderTreeItem);
    }
  }

  public void selectCatalogItem(CatalogItem item) {
    TreeItem<Object> treeItem = findTreeItem(treeRoot, item);
    treeView.getSelectionModel().select(treeItem);
  }

  private TreeItem<Object> findTreeItem(TreeItem<Object> child, CatalogItem item) {
    if(child.getValue().equals(item)) {
      return child;
    }
    ObservableList<TreeItem<Object>> children = child.getChildren();
    for(TreeItem<Object> treeItem : children) {
      TreeItem<Object> match = findTreeItem(treeItem, item);
      if(match != null) {
        return match;
      }
    }
    return null;
  }
}
