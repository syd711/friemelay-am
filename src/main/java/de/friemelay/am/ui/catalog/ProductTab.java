package de.friemelay.am.ui.catalog;

import de.friemelay.am.UIController;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Product;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.ModelTab;
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
public class ProductTab extends ModelTab implements EventHandler<ActionEvent>, ChangeListener<String> {
  private Product product;

  private Button saveButton;
  private Button resetButton;

  private boolean dirty;
  private final VBox form = new VBox();

  public ProductTab(Product product) {
    super(product);
    setGraphic(ResourceLoader.getImageView(product.getStatusIcon()));
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
      boolean confirmed = WidgetFactory.showConfirmation("Produkt überschreiben", "Soll das Produkt mit den Änderungen überschrieben werden?");
      if(confirmed) {
        DB.save(product);
        this.setText(product.toString());
        UIController.getInstance().refreshCatalog();
      }
      setDirty(!confirmed);
    }
  }

  public void reload() {
    createForm();
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
    WidgetFactory.addBindingFormTextfield(detailsForm, "Name:", product.getTitle(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(detailsForm, "Titeltext:", product.getTitleText(), index++, true, this);
    WidgetFactory.addBindingFormTextarea(detailsForm, "Kurzbeschreibung:", product.getDescription(), index++, true, this);
    WidgetFactory.createSection(form, detailsForm, "Produkt-Details", false);
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
  public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    setDirty(true);
  }
}
