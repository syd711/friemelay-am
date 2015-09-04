package de.friemelay.am.ui.imageeditor;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ImageEditor extends BorderPane {

  private ImageTabPane imageTabs;
  private Stage stage;
  private List<ImageEditorChangeListener> changeListeners = new ArrayList<>();
  private boolean dirty = false;
  private int maxTabs = 1;

  public static File lastLocation;

  public ImageEditor(Stage stage, int height) {
    this.stage = stage;
    imageTabs = new ImageTabPane();
    setCenter(imageTabs);
    setMinHeight(height);
    setStyle("-fx-border-color:#DDD; -fx-border-width:1px;");
  }

  public void openTab(ImageVariant variant) {
    imageTabs.getTabs().addAll(new ImageTab(this, variant));
  }

  public void setImageTabCount(int max) {
    this.maxTabs = max;
  }

  public void addChangeListener(ImageEditorChangeListener listener) {
    this.changeListeners.add(listener);
  }

  protected void setDirty(boolean b, ImageVariant variant) {
    this.dirty = b;
    if(dirty) {
      for(ImageEditorChangeListener changeListener : changeListeners) {
        changeListener.imageChanged(new ImageEditorChangeEvent(variant));
      }
    }
  }

  public Stage getStage() {
    return stage;
  }

  public ObservableList<Tab> getTabs() {
    return imageTabs.getTabs();
  }
}
