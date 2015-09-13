package de.friemelay.am.ui.catalog;

import de.friemelay.am.db.DB;
import de.friemelay.am.model.Category;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.imageeditor.ImageEditorChangeEvent;
import de.friemelay.am.ui.imageeditor.ImageEditorChangeListener;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


/**
 *
 */
public class CategoryTab extends CatalogTab<Category> implements EventHandler<ActionEvent>, ImageEditorChangeListener, ChangeListener {

  private VBox catalogForm;

  public CategoryTab(Category category) {
    super(category);
  }

  /**
   * Action handling for all buttons on the pane
   * @param event
   */
  public void handle(ActionEvent event) {
    if(event.getSource() == resetButton) {
      model = DB.getCategory(getModel().getParent(), getModel().getId(), true);
      createCatalogForm();
      setDirty(false);
    }
    else if(event.getSource() == saveButton) {
      doSave();
    }
  }

  public void reload() {
    createCatalogForm();
  }

  @Override
  protected void init() {
    BorderPane root = getRoot();

    ToolBar toolbar = new ToolBar();
    saveButton = new Button("Änderungen speichern", ResourceLoader.getImageView("save.png"));
    saveButton.setOnAction(this);
    saveButton.setDisable(!isDirty());
    resetButton = new Button("Änderungen zurücksetzen", ResourceLoader.getImageView("revert.png"));
    resetButton.setOnAction(this);
    resetButton.setDisable(!isDirty());
    toolbar.getItems().addAll(saveButton, resetButton);
    root.setTop(toolbar);

    catalogForm = new VBox();
    catalogForm.setAlignment(Pos.TOP_CENTER);
    catalogForm.setFillWidth(true);

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    centerScroller.setContent(catalogForm);

    catalogForm.setPadding(new Insets(5, 10, 5, 0));
    root.setCenter(centerScroller);

    setContent(root);

    createCatalogForm();
  }

  private void createCatalogForm() {
    catalogForm.getChildren().clear();

    GridPane categoryDetailsForm = WidgetFactory.createFormGrid();
    int index = 0;
    WidgetFactory.addBindingFormCheckbox(categoryDetailsForm, "Kategorie aktiviert:", getModel().getStatus(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(categoryDetailsForm, "Name:", getModel().getTitle(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Titeltext:", getModel().getDetails(), 200, index++, true, this);
    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Kurzbeschreibung (Bildunterschrift):", getModel().getShortDescription(), index++, true, this);
    String formLabel = "Bild - empfohlene Größe: 305 x 200 Pixel";
    if(getModel().isTopLevel()) {
      formLabel = "Bild - empfohlene Größe: 305 x 130 Pixel";
    }
    formLabel+="\n(keine automatische Skalierung!)";
    WidgetFactory.addFormImageEditor(categoryDetailsForm, formLabel, getModel().getImage(), index++, 400, 1, this);

    String label = "Details der Kategorie";
    if(getModel().isTopLevel()) {
      label = "Details der Top-Level Kategorie";
    }
    WidgetFactory.createSection(catalogForm, categoryDetailsForm, label, false);
  }

  @Override
  public void imageChanged(ImageEditorChangeEvent event) {
    getModel().setImage(event.getImageVariant().getImage());
    setDirtyImages(true);
    setDirty(true);
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {
    setDirty(true);
  }
}
