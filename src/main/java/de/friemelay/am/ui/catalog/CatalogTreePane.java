package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.model.CatalogItem;
import de.friemelay.am.model.Category;
import de.friemelay.am.model.Product;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.util.ProgressForm;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CatalogTreePane extends BorderPane implements EventHandler<MouseEvent> {

  private final TreeItem<Object> treeRoot = new TreeItem<Object>(new Category());
  private TreeView treeView;
  private List<Category> items = new ArrayList<>();
  private Button addVariantButton;

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

    Button addCategoryButton = new Button("", ResourceLoader.getImageView("new_category.png"));
    addCategoryButton.setTooltip(new Tooltip("Neue Kategorie erstellen"));
    addCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Category selection = getSelectedCategory();
        String name = WidgetFactory.showInputDialog("", "Kategorie anlegen", "Name der Kategorie");
        if(!StringUtils.isEmpty(name)) {
          Category category = DB.createCategory(name, selection);
          reload();
          UIController.getInstance().open(category);
        }
      }
    });

    Button addProductButton = new Button("", ResourceLoader.getImageView("new_product.png"));
    addProductButton.setTooltip(new Tooltip("Neues Produkt erstellen"));
    addProductButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Category selection = getSelectedCategory();
        if(selection != null) {
          String name = WidgetFactory.showInputDialog("", "Produkt anlegen", "Name des Produktes");
          if(!StringUtils.isEmpty(name)) {
            Product product = DB.createProduct(name, selection);
            reload();
            UIController.getInstance().open(product);
          }
        }
      }
    });

    addVariantButton = new Button("", ResourceLoader.getImageView("new_variant.png"));
    addVariantButton.setTooltip(new Tooltip("Neue Produktvariante erstellen"));
    addVariantButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        AbstractModel item = getSelection();
        if(item != null && item instanceof Product) {
          String name = WidgetFactory.showInputDialog("", "Variante anlegen", "Name der Variante");
          if(!StringUtils.isEmpty(name)) {
            Product product = DB.createVariant(name, (Product) item);
            reload();
            UIController.getInstance().open(product);
          }
        }
      }
    });

    Button deleteButton = new Button("", ResourceLoader.getImageView("trash.png"));
    deleteButton.setTooltip(new Tooltip("Selektion löschen"));
    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        CatalogItem selection = getSelection();
        if(selection != null) {
          boolean delete = WidgetFactory.showConfirmation("Löschen", "Soll '" + selection + "' gelöscht werden?");
          if(delete) {
            UIController.getInstance().closeTab(selection);
            DB.deleteCatalogItem(selection);
            reload();
          }
        }
      }
    });

    toolbar.getItems().addAll(addCategoryButton, addProductButton, addVariantButton, new Separator(), deleteButton);

    setTop(toolbar);
  }

  public void handle(MouseEvent event) {
    if(event.getClickCount() == 2) {
      UIController.getInstance().open(getSelection());
    }

    AbstractModel model = getSelection();
    if(model instanceof Category) {
      addVariantButton.setDisable(true);
    }
    if(model instanceof Product) {
      Product product = (Product) model;
      if(product.isVariant()) {
        addVariantButton.setDisable(true);
      }
      else {
        addVariantButton.setDisable(false);
      }
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

  public CatalogItem getSelection() {
    TreeItem selectedItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
    if(selectedItem != null) {
      TreeItem<Category> item = (TreeItem<Category>) selectedItem;
      return item.getValue();
    }
    return null;
  }


  public void refresh() {
    refresh(treeRoot);
  }

  private void refresh(TreeItem<Object> item) {
    ObservableList<TreeItem<Object>> children = item.getChildren();
    for(TreeItem<Object> child : children) {
      CatalogItem value = (CatalogItem) child.getValue();
      child.valueProperty().unbind();
      child.valueProperty().bind(new SimpleObjectProperty<Object>(value));
      refresh(child);
    }
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
        items = buildCatalog(items);

        buildTree(items, treeRoot);

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


  private static List<Category> buildCatalog(List<Category> items) {
    List<Category> topLevel = new ArrayList<>();
    Iterator<Category> iterator = items.iterator();
    while(iterator.hasNext()) {
      Category next = iterator.next();
      if(next.isTopLevel()) {
        topLevel.add(next);
      }
    }

    iterator = items.iterator();
    while(iterator.hasNext()) {
      Category next = iterator.next();
      if(next.isTopLevel()) {
        continue;
      }
      Category parent = getParent(next, items);
      if(parent != null) {
        parent.getChildren().add(next);
      }
    }

    return topLevel;
  }

  private static Category getParent(Category next, List<Category> items) {
    for(Category item : items) {
      if(item.getId() == next.getParentId()) {
        return item;
      }
    }
    return null;
  }

  private void buildTree(List<Category> children, TreeItem<Object> parent) {
    for(Category item : children) {
      TreeItem<Object> categoryTreeItem = new TreeItem<Object>(item, ResourceLoader.getImageView(item.getStatusIcon()));
      categoryTreeItem.valueProperty().bind(new SimpleObjectProperty<Object>(item));
      categoryTreeItem.setExpanded(true);

      List<Product> products = item.getProducts();
      for(Product product : products) {
        TreeItem<Object> productTreeItem = new TreeItem<Object>(product, ResourceLoader.getImageView(product.getStatusIcon()));
        productTreeItem.valueProperty().bind(new SimpleObjectProperty<Object>(product));
        productTreeItem.setExpanded(true);
        categoryTreeItem.getChildren().addAll(productTreeItem);

        List<Product> variants = product.getVariants();
        for(Product variant : variants) {
          TreeItem<Object> variantTreeItem = new TreeItem<Object>(variant, ResourceLoader.getImageView(variant.getStatusIcon()));
          variantTreeItem.valueProperty().bind(new SimpleObjectProperty<Object>(variant));
          variantTreeItem.setExpanded(true);
          productTreeItem.getChildren().addAll(variantTreeItem);
        }
      }


      parent.getChildren().add(categoryTreeItem);
      buildTree(item.getChildren(), categoryTreeItem);
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
