package de.friemelay.am.ui.catalog;

import de.friemelay.am.model.Category;
import de.friemelay.am.resources.ResourceLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 *
 */
public class CategoryTab extends Tab implements EventHandler<ActionEvent>, ChangeListener<String> {
  private Category category;

  private Button saveButton;
  private Button resetButton;

  private boolean dirty;
  private final VBox catalogForm = new VBox();

  public CategoryTab(Category category) {
    super(category.toString());
//    setGraphic(ResourceLoader.getImageView(category.getStatusIcon()));
    this.category = category;
    init();
  }

  /**
   * Action handling for all buttons on the pane
   * @param event
   */
  public void handle(ActionEvent event) {
    if(event.getSource() == resetButton) {
//      order = DB.getOrder(order.getId());
//      createCatalogForm();
//      setDirty(false);
    }
    else if(event.getSource() == saveButton) {
//      boolean confirmed = WidgetFactory.showConfirmation("Bestellung überschreiben", "Soll die Bestellung mit den Änderungen überschrieben werden?");
//      if(confirmed) {
//        DB.save(order);
//      }
//      setDirty(!confirmed);
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
//    orderForm.getChildren().clear();
//    createStatusGroup(orderForm);
//
//    GridPane orderDetailsForm = WidgetFactory.createFormGrid();
//    orderForm.getStyleClass().add("root");
//    int index = 0;
//    WidgetFactory.addFormLabel(orderDetailsForm, "Bestellnummer:", String.valueOf(order.getId()), index++);
//    WidgetFactory.addFormLabel(orderDetailsForm, "Eingang:", String.valueOf(order.getFormattedCreationDateTime()), index++);
//    WidgetFactory.addBindingFormLabel(orderDetailsForm, "Name:", order.getCustomer().getAddress().getLastname(), index++, null);
//    WidgetFactory.addBindingFormLabel(orderDetailsForm, "Vorname:", order.getCustomer().getAddress().getFirstname(), index++, null);
//    WidgetFactory.addFormLabel(orderDetailsForm, "Preis:", index++, order.getTotalPrice(), new TotalPriceConverter(order));
//    WidgetFactory.addFormLabel(orderDetailsForm, "Preis inkl. Versandkosten:", index++, order.getTotalPrice(), new TotalPriceWithShippingConverter(order));
//    WidgetFactory.addFormLabel(orderDetailsForm, "Zahlungsweise:", order.getFormattedPaymentType(), index++);
//    WidgetFactory.addBindingFormTextarea(orderDetailsForm, "Anmerkungen vom Kunden:", order.getComments(), index++, !isReadonly(), this);
//    WidgetFactory.createSection(orderForm, orderDetailsForm, "Details der Bestellung");
//
//    GridPane addressForm = WidgetFactory.createFormGrid();
//    index = 0;
//    WidgetFactory.addBindingFormTextfield(addressForm, "Name:", order.getCustomer().getAddress().getLastname(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "Vorname:", order.getCustomer().getAddress().getFirstname(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "E-Mail:", order.getCustomer().getEmail(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "Telefon:", order.getCustomer().getPhone(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "Firma:", order.getCustomer().getAddress().getCompany(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "Straße:", order.getCustomer().getAddress().getStreet(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "Adresszusatz:", order.getCustomer().getAddress().getAdditional(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "PLZ:", order.getCustomer().getAddress().getZip(), index++, !isReadonly(), this);
//    WidgetFactory.addBindingFormTextfield(addressForm, "Ort:", order.getCustomer().getAddress().getCity(), index++, !isReadonly(), this);
//    WidgetFactory.createSection(orderForm, addressForm, "Kundendaten", true);
//
//    if(order.getCustomer().getBillingAddress() != null) {
//      GridPane billingAddressForm = WidgetFactory.createFormGrid();
//      index = 0;
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Name:", order.getCustomer().getBillingAddress().getLastname(), index++, !isReadonly(), this);
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Vorname:", order.getCustomer().getBillingAddress().getFirstname(), index++, !isReadonly(), this);
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Firma:", order.getCustomer().getBillingAddress().getCompany(), index++, !isReadonly(), this);
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Straße:", order.getCustomer().getBillingAddress().getStreet(), index++, !isReadonly(), this);
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Adresszusatz:", order.getCustomer().getBillingAddress().getAdditional(), index++, !isReadonly(), this);
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "PLZ:", order.getCustomer().getBillingAddress().getZip(), index++, !isReadonly(), this);
//      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Ort:", order.getCustomer().getBillingAddress().getCity(), index++, !isReadonly(), this);
//      WidgetFactory.createSection(orderForm, billingAddressForm, "Rechnungsadresse", true);
//    }
//
//    createOrderItemsGroup();
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

  public Category getCategory() {
    return category;
  }
}
