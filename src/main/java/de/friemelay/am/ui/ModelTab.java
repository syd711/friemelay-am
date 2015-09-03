package de.friemelay.am.ui;

import de.friemelay.am.model.AbstractModel;
import de.friemelay.am.resources.ResourceLoader;
import javafx.scene.control.Tab;

/**
 *
 */
public class ModelTab extends Tab {
  private AbstractModel model;

  public ModelTab(AbstractModel model) {
    super(model.toString());
    setGraphic(ResourceLoader.getImageView(model.getStatusIcon()));
    this.model = model;
  }

  public AbstractModel getModel() {
    return model;
  }
}
