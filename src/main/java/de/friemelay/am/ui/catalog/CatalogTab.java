package de.friemelay.am.ui.catalog;

import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.ui.ModelTab;
import javafx.scene.layout.BorderPane;

/**
 *
 */
abstract class CatalogTab<T extends AbstractModel> extends ModelTab<T> {

  private BorderPane catalogTabRoot;
  private PreviewPanel previewPanel;

  public CatalogTab(T model) {
    super(model);
  }

//  @Override
//  protected void initTab() {
//    catalogTabRoot = new BorderPane();
//    init();
//
//    SplitPane splitPane = new SplitPane();
//
//    BorderPane newRoot = new BorderPane();
//    previewPanel = new PreviewPanel(splitPane);
//    splitPane.getItems().addAll(catalogTabRoot, previewPanel);
//    newRoot.setCenter(splitPane);
//    setContent(newRoot);
//
//    saveButton.pressedProperty().addListener(new ChangeListener<Boolean>() {
//      @Override
//      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//        previewPanel.refresh();
//      }
//    });
//    resetButton.pressedProperty().addListener(new ChangeListener<Boolean>() {
//      @Override
//      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//        previewPanel.refresh();
//      }
//    });
//  }
//
//  @Override
//  protected BorderPane getRoot() {
//    return catalogTabRoot;
//  }
}
