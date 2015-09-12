package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Product;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.imageeditor.ImageEditor;
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
public class ProductTab extends CatalogTab<Product> implements EventHandler<ActionEvent>, ChangeListener, ImageEditorChangeListener {
  private VBox form;
  private ImageEditor categoryImageEditor;

  public ProductTab(Product product) {
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

    GridPane detailsForm = WidgetFactory.createFormGrid();
    detailsForm.getStyleClass().add("root");
    int index = 0;
    WidgetFactory.addBindingFormCheckbox(detailsForm, "Produkt aktiviert:", getModel().getStatus(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(detailsForm, "Name (Bildüberschrift):", getModel().getTitle(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(detailsForm, "Kurzbeschreibung (Bildunterschrift):", getModel().getShortDescription(), index++, true, this);
    WidgetFactory.addBindingFormCheckbox(detailsForm, "Anzahl-Auswahl anzeigen:", getModel().getAmount(), index++, true, this);
    WidgetFactory.createSection(form, detailsForm, "Produkt-Details", false);

    GridPane categoryImageForm = WidgetFactory.createFormGrid();
    categoryImageForm.getStyleClass().add("root");
    index = 0;
    String formLabel = "Kategoriebild - empfohlene Größe: 305x 200 Pixel\n(keine automatische Skalierung!)";
    categoryImageEditor = WidgetFactory.addFormImageEditor(categoryImageForm, formLabel, getModel().getImage(), index++, 250, 1, this);
    WidgetFactory.createSection(form, categoryImageForm, "Kategoriebild", false);

    GridPane variantForm = WidgetFactory.createFormGrid();
    variantForm.getStyleClass().add("root");
    index = 0;
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Überschrift:", getModel().getVariantLabel(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Name:", getModel().getVariantName(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Kurzbeschreibung:", getModel().getVariantShortDescription(), index++, true, this);
    WidgetFactory.addBindingFormSpinner(variantForm, "Warenbestand:", 0, 1000, getModel().getStock(), index++, true, this);
    WidgetFactory.addBindingFormPriceField(variantForm, "Preis:", getModel().getPrice(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(variantForm, "Produktbeschreibung:", getModel().getDetails(), 400, index++, true, this);
    WidgetFactory.addFormImageEditor(variantForm, "Produktbilder - empfohlene Größe: 800 x 600 Pixel:\n" +
        "(automatische Skalierung größerer Bilder)", getModel().getImages(), index++, 400, 10, this);

    WidgetFactory.createSection(form, variantForm, "Produkt Details (diese werden nur benutzt wenn das Produkt keine Varianten hat oder keine aktiviert ist)", !getModel().getVariants().isEmpty());
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {
    setDirty(true);
  }

  @Override
  public void imageChanged(ImageEditorChangeEvent event) {
    if(event.getSource() == categoryImageEditor) {
      getModel().setImage(event.getImage());
    }
    else {
      getModel().setImages(event.getAllImages());
    }

    setDirty(true);
  }
}
