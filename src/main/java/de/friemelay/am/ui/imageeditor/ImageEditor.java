package de.friemelay.am.ui.imageeditor;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
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
  private int maxImageWidth;
  private int maxImageHeight;

  public static File lastLocation;

  public ImageEditor(Stage stage, int height, int maxTabs, int maxImageWidth, int maxImageHeight) {
    this.stage = stage;
    imageTabs = new ImageTabPane();
    setCenter(imageTabs);
    setMinHeight(height);
    this.maxImageHeight = maxImageHeight;
    this.maxImageWidth = maxImageWidth;
    this.maxTabs = maxTabs;
    setStyle("-fx-border-color:#DDD; -fx-border-width:1px;");
  }

  public void openTab(String name, BufferedImage image) {
    ImageVariant variant = new ImageVariant(name, image, maxImageWidth, maxImageHeight);
    ImageTab imageTab = new ImageTab(this, variant);
    imageTabs.getTabs().addAll(imageTab);
    imageTabs.getSelectionModel().select(imageTab);
  }

  public void addChangeListener(ImageEditorChangeListener listener) {
    this.changeListeners.add(listener);
  }

  protected void setDirty(boolean b, ImageVariant variant) {
    this.dirty = b;
    if(dirty) {
      List<ImageVariant> variants = getAllVariants();
      for(ImageEditorChangeListener changeListener : changeListeners) {
        changeListener.imageChanged(new ImageEditorChangeEvent(variant, variants));
      }
    }
  }

  public Stage getStage() {
    return stage;
  }

  public ObservableList<Tab> getTabs() {
    return imageTabs.getTabs();
  }

  public int getMaxTabs() {
    return maxTabs;
  }

  public List<ImageVariant> getAllVariants() {
    List<ImageVariant> variants = new ArrayList<>();
    ObservableList<Tab> tabs = imageTabs.getTabs();
    for(Tab tab : tabs) {
      ImageTab imageTab = (ImageTab) tab;
      ImageVariant variant = imageTab.getVariant();
      if(variant.getImage() != null) {
        variants.add(variant);
      }
    }

    return variants;
  }
}
