package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Product;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.ModelTab;
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
public class ProductTab extends ModelTab implements EventHandler<ActionEvent>, ChangeListener, ImageEditorChangeListener {
  private Product product;

  private Button saveButton;
  private Button resetButton;

  private boolean dirty;
  private final VBox form = new VBox();

  public ProductTab(Product product) {
    super(product);
    this.product = product;
    init();
  }

  /**
   * Action handling for all buttons on the pane
   * @param event
   */
  public void handle(ActionEvent event) {
    if(event.getSource() == resetButton) {
      product = DB.getProduct(product.getId());
      createForm();
      setDirty(false);
    }
    else if(event.getSource() == saveButton) {
      DB.save(product);
      this.setText(product.toString());
      UIController.getInstance().refreshCatalog();
      setDirty(false);
    }
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

    form.setAlignment(Pos.TOP_CENTER);
    form.setFillWidth(true);

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    centerScroller.setContent(form);

    form.setPadding(new Insets(5, 10, 5, 0));
    root.setCenter(centerScroller);

    setContent(root);

    createForm();
  }

  private void createForm() {
    form.getChildren().clear();

    GridPane detailsForm = WidgetFactory.createFormGrid();
    detailsForm.getStyleClass().add("root");
    int index = 0;
    WidgetFactory.addBindingFormTextfield(detailsForm, "Name (Bildüberschrift):", product.getTitle(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(detailsForm, "Kurzbeschreibung (Bildunterschrift):", product.getShortDescription(), index++, true, this);
    WidgetFactory.addBindingFormCheckbox(detailsForm, "Anzahl-Auswahl anzeigen:", product.getAmount(), index++, true, this);
    WidgetFactory.createSection(form, detailsForm, "Produkt-Details", false);

    GridPane variantForm = WidgetFactory.createFormGrid();
    variantForm.getStyleClass().add("root");
    index = 0;
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Überschrift:", product.getVariantLabel(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Name:", product.getVariantName(), index++, true, this);
    WidgetFactory.addBindingFormTextfield(variantForm, "Varianten-Kurzbeschreibung:", product.getVariantShortDescription(), index++, true, this);
    WidgetFactory.addBindingFormSpinner(variantForm, "Warenbestand:", 0, 1000, product.getStock(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(variantForm, "Produktbeschreibung:", product.getDetails(), 100, index++, true, this);
    String formLabel = "Produktbilder - empfohlene Größe: 800 x 600 Pixel:\n(automatische Skalierung größerer Bilder)";
    WidgetFactory.addFormImageEditor(variantForm, formLabel, product.getImages(), index++, 400, 10, this);
    WidgetFactory.createSection(form, variantForm, "Produkt Details (diese werden nur benutzt wenn das Produkt keine Varianten hat)", false);
  }


  private void setDirty(boolean dirty) {
    this.dirty = dirty;
    saveButton.setDisable(!dirty);
    resetButton.setDisable(!dirty);
  }

  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {
    setDirty(true);
  }

  @Override
  public void imageChanged(ImageEditorChangeEvent event) {
    product.setImages(event.getAllImages());
    setDirty(true);
  }
}
