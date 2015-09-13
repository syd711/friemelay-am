package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.model.Category;
import de.friemelay.am.model.Product;
import de.friemelay.am.ui.ModelTab;
import de.friemelay.am.ui.util.ProgressForm;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public abstract class CatalogTab<T extends AbstractModel> extends ModelTab<T> {

  private BorderPane catalogTabRoot;
  private PreviewPanel previewPanel;

  private boolean dirtyImages = false;

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

  protected void setDirtyImages(boolean b) {
    this.dirtyImages = b;
  }

  protected void doSave() {
    String msg = "Speichere '" + model + "'";
    if(dirtyImages) {
      msg = msg+ " + Bilder";
    }
    ProgressForm pForm = new ProgressForm(UIController.getInstance().getStage().getScene(), msg);

    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws InterruptedException {
        AbstractModel model = getModel();
        if(model instanceof Category) {
          DB.save((Category) model, dirtyImages);
        }
        else if(model instanceof Product) {
          DB.save((Product) model, dirtyImages);
        }
        updateProgress(10, 10);
        return null;
      }
    };

    // binds progress of progress bars to progress of task:
    pForm.activateProgressBar(task);

    task.setOnSucceeded(event -> {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          setText(getModel().toString());
          UIController.getInstance().refreshCatalog();
          setDirty(false);
          setDirtyImages(false);
          pForm.getDialogStage().close();
        }
      });

    });

    pForm.getDialogStage().show();
    Thread thread = new Thread(task);
    thread.start();


  }
}
