package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Product;
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
public class VariantTab extends CatalogTab<Product> implements EventHandler<ActionEvent>, ChangeListener, ImageEditorChangeListener {
  private VBox form;

  public VariantTab(Product product) {
    super(product);
  }

  /**
   * Action handling for all buttons on the pane
   * @param event
   */
  public void handle(ActionEvent event) {
    if(event.getSource() == resetButton) {
      model = DB.getProduct(getModel().getParent(), getModel().getId());
      createForm();
      setDirty(false);
    }
    else if(event.getSource() == saveButton) {
      DB.save(getModel());
      this.setText(getModel().toString());
      UIController.getInstance().refreshCatalog();
      setDirty(false);
    }
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

    form = new VBox();
    form.setAlignment(Pos.TOP_CENTER);
    form.setFillWidth(true);

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    centerScroller.setContent(form);

    form.setPadding(new Insets(5, 10, 5, 0));
    root.setCenter(centerScroller);

    createForm();
  }

  private void createForm() {
    form.getChildren().clear();

    GridPane variantForm = WidgetFactory.createFormGrid();
    variantForm.getStyleClass().add("root");
    int index = 0;
    WidgetFactory.addBindingFormCheckbox(variantForm, "Variante aktiviert:", getModel().getStatus(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Überschrift:", getModel().getVariantLabel(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Name:", getModel().getTitle(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Kurzbeschreibung:", getModel().getVariantShortDescription(), index++, true, this);
    WidgetFactory.addBindingFormSpinner(variantForm, "Warenbestand:", 0, 1000, getModel().getStock(), index++, true, this);
    WidgetFactory.addBindingFormPriceField(variantForm, "Preis:", getModel().getPrice(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(variantForm, "Produktbeschreibung:", getModel().getDetails(), 180, index++, true, this);
    String formLabel = "Variantenbilder - empfohlene Größe: 800 x 600 Pixel:\n(automatische Skalierung größerer Bilder)";
    WidgetFactory.addFormImageEditor(variantForm, formLabel, getModel().getImages(), index++, 500, 1, this);
    WidgetFactory.createSection(form, variantForm, "Varianten Details (alle übrigen Einstellungen werden vom Produkt benutzt)", false);
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {
    setDirty(true);
  }

  @Override
  public void imageChanged(ImageEditorChangeEvent event) {
    getModel().setImages(event.getAllImages());
    setDirty(true);
  }
}
