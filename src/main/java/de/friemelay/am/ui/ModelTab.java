package de.friemelay.am.ui;

import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.resources.ResourceLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public abstract class ModelTab<T extends AbstractModel> extends Tab {
  protected T model;
  private boolean dirty;

  private BorderPane root = new BorderPane();


  protected Button saveButton;
  protected Button resetButton;

  public ModelTab(T model) {
    super(model.toString());
    setGraphic(ResourceLoader.getImageView(model.getStatusIcon()));
    this.model = model;
    initTab();
  }

  protected void initTab() {
    this.setContent(root);
    init();
  }

  protected boolean isDirty() {
    return dirty;
  }

  public T getModel() {
    return model;
  }

  protected void setDirty(boolean b) {
    this.dirty = b;
    saveButton.setDisable(!b);
    resetButton.setDisable(!b);
  }

  protected BorderPane getRoot() {
    return root;
  }

  abstract protected void init();
}
