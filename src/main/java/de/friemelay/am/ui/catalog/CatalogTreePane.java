package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.*;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.util.ProgressForm;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CatalogTreePane extends BorderPane implements EventHandler<MouseEvent> {

  private final TreeItem<CatalogItem> treeRoot;
  private Category root = new Category(null);
  private TreeView treeView;
  private List<Category> items = new ArrayList<>();

  private Button addVariantButton;
  private Button addProductButton;
  private Button addCategoryButton;
  private Button deleteButton;

  private CatalogTreeExpandListener expandListener = new CatalogTreeExpandListener();

  public CatalogTreePane() {
    root.setStatus(1);
    treeRoot = new TreeItem<CatalogItem>(root);
    treeView = new TreeView<CatalogItem>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(false);
    treeView.setRoot(treeRoot);
    treeView.setCellFactory(new Callback<TreeView<CatalogItem>, TreeCell<CatalogItem>>() {
      @Override
      public TreeCell<CatalogItem> call(TreeView<CatalogItem> p) {
        return new CatalogTreeCell();
      }
    });

    setCenter(treeView);

    ToolBar toolbar = new ToolBar();
    Button refreshButton = new Button("", ResourceLoader.getImageView("refresh.png"));
    refreshButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        reloadAndOpen(-1, -1);
      }
    });
    refreshButton.setTooltip(new Tooltip("Katalog neu laden"));
    toolbar.getItems().add(refreshButton);

    addCategoryButton = new Button("", ResourceLoader.getImageView("new_category.png"));
    addCategoryButton.setTooltip(new Tooltip("Neue Kategorie erstellen"));
    addCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Category selection = getSelectedCategory();
        String name = WidgetFactory.showInputDialog("", "Kategorie anlegen", "Name der Kategorie");
        if(!StringUtils.isEmpty(name)) {
          Category category = DB.createCategory(name.trim(), selection);
          reloadAndOpen(category.getId(), AbstractModel.TYPE_CATEGORY);
        }
      }
    });

    addProductButton = new Button("", ResourceLoader.getImageView("new_product.png"));
    addProductButton.setTooltip(new Tooltip("Neues Produkt erstellen"));
    addProductButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Category selection = getSelectedCategory();
        if(selection != null) {
          String name = WidgetFactory.showInputDialog("", "Produkt anlegen", "Name des Produktes");
          if(!StringUtils.isEmpty(name)) {
            Product product = DB.createProduct(selection, name.trim(), selection.getId(), false);
            reloadAndOpen(product.getId(), AbstractModel.TYPE_PRODUCT);
            addProductButton.setDisable(true);
          }
        }
      }
    });

    addVariantButton = new Button("", ResourceLoader.getImageView("new_variant.png"));
    addVariantButton.setTooltip(new Tooltip("Neue Produktvariante erstellen"));
    addVariantButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        CatalogItem item = getSelection();
        if(item != null && item instanceof Product) {
          String name = WidgetFactory.showInputDialog("", "Variante anlegen", "Name der Variante");
          if(!StringUtils.isEmpty(name)) {
            Product product = DB.createProduct(item, name.trim(), item.getId(), true);
            reloadAndOpen(product.getId(), AbstractModel.TYPE_VARIANT);
          }
        }
      }
    });

    deleteButton = new Button("", ResourceLoader.getImageView("trash.png"));
    deleteButton.setTooltip(new Tooltip("Selektion löschen"));
    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        CatalogItem selection = getSelection();
        if(selection != null) {
          boolean delete = WidgetFactory.showConfirmation("Löschen", "Soll '" + selection + "' gelöscht werden?");
          if(delete) {
            UIController.getInstance().closeTab(selection);
            DB.deleteCatalogItem(selection);
            reloadAndOpen(-1, -1);
          }
        }
      }
    });


    Button expandButton = new Button("", ResourceLoader.getImageView("expand.png"));
    expandButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        expandAll(treeRoot.getChildren(), true);
      }
    });
    Button collapseButton = new Button("", ResourceLoader.getImageView("collapse.png"));
    collapseButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        expandAll(treeRoot.getChildren(), false);
      }
    });

    toolbar.getItems().addAll(new Separator(), addCategoryButton, addProductButton, addVariantButton,
        new Separator(), expandButton, collapseButton, new Separator(), deleteButton);

    setTop(toolbar);
  }

  public void handle(MouseEvent event) {
    if(event != null) {
      if(event.getClickCount() == 2) {
        CatalogItem selection = getSelection();
        if(selection != null) {
          TreeItem selectedItem = (TreeItem) treeView.getSelectionModel().getSelectedItem();
          selectedItem.setExpanded(true);
          UIController.getInstance().open(selection);
        }
      }
    }

    UIController.getInstance().setInfoMessage("");
    CatalogItem model = getSelection();
    if(model != null && !model.getStatus().get()) {
      UIController.getInstance().setInfoMessage("'" + model + "' ist nicht aktiv und wird nicht auf der Webseite angezeigt.");
    }

    if(model == null) {
      addCategoryButton.setDisable(false);
      addVariantButton.setDisable(true);
      addProductButton.setDisable(true);
      deleteButton.setDisable(true);
    }
    else {
      deleteButton.setDisable(false);
      if(model instanceof Category) {
        addCategoryButton.setDisable(false);
        addVariantButton.setDisable(true);
        addProductButton.setDisable(false);
        }
      if(model instanceof Product) {
        addCategoryButton.setDisable(true);
        Product product = (Product) model;
        if(!product.getStatus().get()) {
          UIController.getInstance().setInfoMessage("'" + model + "' ist nicht aktiv und wird nicht auf der Webseite angezeigt.");
        }
        else if(!product.isOnStock()) {
          UIController.getInstance().setInfoMessage("'" + model + "' ist nicht mehr auf Lager. Sobald alle Varianten eines Produktes nicht mehr auf Lager sind, sollte dieses deaktiviert werden.");
        }
        else if(product.isForFree() && (product.isVariant() || product.getVariants().isEmpty())) {
          UIController.getInstance().setInfoMessage("'" + model + "' hat keinen Preis definiert.");
        }

        if(product.isVariant()) {
          addVariantButton.setDisable(true);
          addProductButton.setDisable(true);
        }
        else {
          addVariantButton.setDisable(false);
        }
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


  private void refresh(TreeItem<CatalogItem> item) {
    ObservableList<TreeItem<CatalogItem>> children = item.getChildren();
    for(TreeItem<CatalogItem> child : children) {
      CatalogItem value = child.getValue();
      child.valueProperty().unbind();
      child.valueProperty().bind(new SimpleObjectProperty<>(value));
      refresh(child);
    }
  }

  public void reloadAndOpen(int id, int type) {
    ProgressForm pForm = new ProgressForm(UIController.getInstance().getStage().getScene(), "Lade Katalog");

    // In real life this task would do something useful and return
    // some meaningful result:
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws InterruptedException {
        root.getChildren().clear();
        items = DB.loadCatalog(root);

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
          pForm.getDialogStage().close();
          treeRoot.getChildren().removeAll(treeRoot.getChildren());
          for(Category topLevel : items) {
            root.getChildren().add(topLevel);
          }

          items = buildCatalog(items);
          buildTree(items, treeRoot);
          treeRoot.setExpanded(true);

          UIController.getInstance().closeInvalidTabs();
          UIController.getInstance().open(getModelFromTree(id, type));
        }
      });

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

  private void buildTree(List<Category> children, TreeItem<CatalogItem> parent) {
    Collections.sort(children, new ModelComparator());
    for(Category item : children) {
      TreeItem<CatalogItem> categoryTreeItem = new TreeItem<>(item, ResourceLoader.getImageView(item.getStatusIcon()));
      categoryTreeItem.expandedProperty().addListener(expandListener);
      categoryTreeItem.valueProperty().bind(new SimpleObjectProperty<>(item));
      categoryTreeItem.setExpanded(getExpandState(item));
      parent.getChildren().add(categoryTreeItem);

      List<Product> products = item.getProducts();
      Collections.sort(products, new ModelComparator());
      for(Product product : products) {
        TreeItem<CatalogItem> productTreeItem = new TreeItem<>(product, ResourceLoader.getImageView(product.getStatusIcon()));
        productTreeItem.expandedProperty().addListener(expandListener);
        productTreeItem.valueProperty().bind(new SimpleObjectProperty<>(product));
        productTreeItem.setExpanded(getExpandState(product));
        categoryTreeItem.getChildren().addAll(productTreeItem);

        List<Product> variants = product.getVariants();
        Collections.sort(variants, new ModelComparator());
        for(Product variant : variants) {
          TreeItem<CatalogItem> variantTreeItem = new TreeItem<>(variant, ResourceLoader.getImageView(variant.getStatusIcon()));
          variantTreeItem.expandedProperty().addListener(expandListener);
          variantTreeItem.valueProperty().bind(new SimpleObjectProperty<>(variant));
          variantTreeItem.setExpanded(getExpandState(variant));
          productTreeItem.getChildren().addAll(variantTreeItem);
        }
      }


      buildTree(item.getChildren(), categoryTreeItem);
    }
  }

  private boolean getExpandState(CatalogItem model) {
    return expandListener.getExpandState(model);
  }

  public void selectCatalogItem(CatalogItem item) {
    TreeItem<CatalogItem> treeItem = findTreeItem(treeRoot, item);
    treeView.getSelectionModel().select(treeItem);
  }

  public boolean hasCatalogItem(CatalogItem item) {
    TreeItem<CatalogItem> treeItem = findTreeItem(treeRoot, item);
    return treeItem != null;
  }

  private TreeItem<CatalogItem> findTreeItem(TreeItem<CatalogItem> child, CatalogItem item) {
    CatalogItem treeModel = child.getValue();
    if(treeModel.getId() == item.getId() && treeModel.getType() == item.getType()) {
      return child;
    }
    ObservableList<TreeItem<CatalogItem>> children = child.getChildren();
    for(TreeItem<CatalogItem> treeItem : children) {
      TreeItem<CatalogItem> match = findTreeItem(treeItem, item);
      if(match != null) {
        return match;
      }
    }
    return null;
  }

  public CatalogItem getModelFromTree(int id, int type) {
    return findModel(root, id, type);
  }

  private CatalogItem findModel(Category category, int id, int type) {
    List<Product> products = category.getProducts();
    for(Product product : products) {
      if(type == AbstractModel.TYPE_PRODUCT) {
        if(product.getId() == id && product.getType() == type) {
          return product;
        }
      }
      else if(type == AbstractModel.TYPE_VARIANT) {
        List<Product> variants = product.getVariants();
        for(Product variant : variants) {
          if(variant.getId() == id) {
            return variant;
          }
        }
      }
    }
    List<Category> children = category.getChildren();
    for(Category child : children) {
      if(child.getId() == id && type == child.getType()) {
        return child;
      }

      CatalogItem model = findModel(child, id, type);
      if(model != null) {
        return model;
      }
    }

    return null;
  }

  private void expandAll(List<TreeItem<CatalogItem>> children, boolean b) {
    for(TreeItem<CatalogItem> child : children) {
      child.setExpanded(b);
      expandAll(child.getChildren(), b);
    }
  }
}
