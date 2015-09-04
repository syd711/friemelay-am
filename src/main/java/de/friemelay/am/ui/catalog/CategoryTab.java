package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Category;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.ModelTab;
import de.friemelay.am.ui.imageeditor.ImageEditor;
import de.friemelay.am.ui.imageeditor.ImageEditorChangeEvent;
import de.friemelay.am.ui.imageeditor.ImageEditorChangeListener;
import de.friemelay.am.ui.imageeditor.ImageVariant;
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
public class CategoryTab extends ModelTab implements EventHandler<ActionEvent>, ImageEditorChangeListener, ChangeListener<String> {
  private Category category;

  private Button saveButton;
  private Button resetButton;

  private boolean dirty;
  private final VBox catalogForm = new VBox();

  public CategoryTab(Category category) {
    super(category);
    this.category = category;
    init();
  }

  /**
   * Action handling for all buttons on the pane
   * @param event
   */
  public void handle(ActionEvent event) {
    if(event.getSource() == resetButton) {
      category = DB.getCategory(category.getId());
      createCatalogForm();
      setDirty(false);
    }
    else if(event.getSource() == saveButton) {
      DB.save(category);
      this.setText(category.toString());
      UIController.getInstance().refreshCatalog();
      setDirty(false);
    }
  }

  public void reload() {
    createCatalogForm();
  }

  private void init() {
    BorderPane root = new BorderPane();

    ToolBar toolbar = new ToolBar();
    saveButton = new Button("Änderungen speichern", ResourceLoader.getImageView("save.png"));
    saveButton.setOnAction(this);
    saveButton.setDisable(!isDirty());
    resetButton = new Button("Änderungen zurücksetzen", ResourceLoader.getImageView("revert.png"));
    resetButton.setOnAction(this);
    resetButton.setDisable(!isDirty());
    toolbar.getItems().addAll(saveButton, resetButton);
    root.setTop(toolbar);

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
    WidgetFactory.addBindingFormTextfield(categoryDetailsForm, "Name:", category.getTitle(), index++, true, this);
    if(!category.isTopLevel()) {
      WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Titeltext:", category.getShortDescription(), index++, true, this);
    }
    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Kurzbeschreibung (Bildunterschrift):", category.getDetails(), index++, true, this);
    ImageEditor imageEditor = WidgetFactory.addFormImageEditor(categoryDetailsForm, "Bild:", index++, 400, this);
    imageEditor.openTab(new ImageVariant("Kategorie Bild", category.getImage()));

    String label = "Details der Kategorie";
    if(category.isTopLevel()) {
      label = "Details der Top-Level Kategorie";
    }
    WidgetFactory.createSection(catalogForm, categoryDetailsForm, label, false);
  }


  private void setDirty(boolean dirty) {
    this.dirty = dirty;
    saveButton.setDisable(!dirty);
    resetButton.setDisable(!dirty);
  }

  public boolean isDirty() {
    return dirty;
  }


  public Category getCategory() {
    return category;
  }

  @Override
  public void imageChanged(ImageEditorChangeEvent event) {
    category.setImage(event.getImageVariant().getImage());
    setDirty(true);
  }

  @Override
  public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    setDirty(true);
  }
}
