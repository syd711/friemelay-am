package de.friemelay.am.ui;

import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.util.MailDialog;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;


/**
 *
 */
public class OrderTab extends Tab implements EventHandler<ActionEvent> {
  private Order order;

  private Button contactButton;
  private Button orderConfirmButton;
  private Button deliveryConfirmButton;

  public OrderTab(Order order) {
    super(order.toString());
    this.order = order;
    init();
  }

  private void init() {
    BorderPane root = new BorderPane();

    ToolBar toolbar = new ToolBar();
    contactButton = new Button("Käufer kontaktieren", ResourceLoader.getImageView("email.png"));
    contactButton.setOnAction(this);
    toolbar.getItems().add(contactButton);
    root.setTop(toolbar);

    VBox center = new VBox();
    center.setAlignment(Pos.TOP_CENTER);
    center.setFillWidth(true);

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    centerScroller.setContent(center);

    center.setPadding(new Insets(5, 10, 5, 0));
    root.setCenter(centerScroller);

    setContent(root);

    createStatusGroup(center);

    GridPane orderForm = WidgetFactory.createFormGrid();
    orderForm.getStyleClass().add("root");
    int index = 0;
    WidgetFactory.addFormLabel(orderForm, "Bestellnummer:", String.valueOf(order.getId()), index++);
    WidgetFactory.addFormLabel(orderForm, "Eingang:", String.valueOf(order.getFormattedCreationDate()), index++);
    WidgetFactory.addFormLabel(orderForm, "Kundenname:", order.getCustomer().getAddress().getFirstname() + " " + order.getCustomer().getAddress().getLastname(), index++);
    WidgetFactory.addFormLabel(orderForm, "Preis inkl. Versandkosten:", String.valueOf(order.getFormattedTotalPrice()), index++);
    WidgetFactory.addFormLabel(orderForm, "Zahlungsweise:", order.getFormattedPaymentType(), index++);
    WidgetFactory.addFormLabel(orderForm, "Anmerkungen vom Kunden:", order.getComments(), index++);
    WidgetFactory.createSection(center, orderForm, "Details der Bestellung");

    GridPane addressForm = WidgetFactory.createFormGrid();
    index = 0;
    WidgetFactory.addFormTextfield(addressForm, "Name:", order.getCustomer().getAddress().getLastname(), index++);
    WidgetFactory.addFormTextfield(addressForm, "Vorname:", order.getCustomer().getAddress().getFirstname(), index++);
    WidgetFactory.addFormTextfield(addressForm, "E-Mail:", order.getCustomer().getEmail(), index++);
    WidgetFactory.addFormTextfield(addressForm, "Telefon:", order.getCustomer().getPhone(), index++);
    WidgetFactory.addFormTextfield(addressForm, "Firma:", order.getCustomer().getAddress().getCompany(), index++);
    WidgetFactory.addFormTextfield(addressForm, "Straße:", order.getCustomer().getAddress().getStreet(), index++);
    WidgetFactory.addFormTextfield(addressForm, "Adresszusatz:", order.getCustomer().getAddress().getAdditional(), index++);
    WidgetFactory.addFormTextfield(addressForm, "PLZ:", order.getCustomer().getAddress().getZip(), index++);
    WidgetFactory.addFormTextfield(addressForm, "Ort:", order.getCustomer().getAddress().getCity(), index++);
    WidgetFactory.createSection(center, addressForm, "Kundendaten", true);

    GridPane billingAddressForm = WidgetFactory.createFormGrid();
    index = 0;
    WidgetFactory.addFormTextfield(billingAddressForm, "Name:", order.getCustomer().getBillingAddress().getLastname(), index++);
    WidgetFactory.addFormTextfield(billingAddressForm, "Vorname:", order.getCustomer().getBillingAddress().getFirstname(), index++);
    WidgetFactory.addFormTextfield(billingAddressForm, "Firma:", order.getCustomer().getBillingAddress().getCompany(), index++);
    WidgetFactory.addFormTextfield(billingAddressForm, "Straße:", order.getCustomer().getBillingAddress().getStreet(), index++);
    WidgetFactory.addFormTextfield(billingAddressForm, "Adresszusatz:", order.getCustomer().getBillingAddress().getAdditional(), index++);
    WidgetFactory.addFormTextfield(billingAddressForm, "PLZ:", order.getCustomer().getBillingAddress().getZip(), index++);
    WidgetFactory.addFormTextfield(billingAddressForm, "Ort:", order.getCustomer().getBillingAddress().getCity(), index++);
    WidgetFactory.createSection(center, billingAddressForm, "Rechnungsadresse", true);

    GridPane itemsForm = WidgetFactory.createFormGrid();
    index = 0;
    WidgetFactory.createSection(center, itemsForm, "Bestellung", false);
    List<OrderItem> orderItems = getOrder().getOrderItems();
    for(OrderItem orderItem : orderItems) {
      createOrderItem(itemsForm, orderItem, index++);
    }
  }

  private void createStatusGroup(Pane center) {
    HBox statusPanel = new HBox(10);
    statusPanel.getStyleClass().add("root");
    statusPanel.setAlignment(Pos.BASELINE_LEFT);
    ImageView arrow = ResourceLoader.getImageView("right-arrow.png");
    VBox imageWrapper = new VBox();
    imageWrapper.getChildren().add(arrow);

    orderConfirmButton = WidgetFactory.createButton(statusPanel, "Auftragsbestätigung senden", "check-green.png", this);
    statusPanel.getChildren().addAll(imageWrapper);
    deliveryConfirmButton = WidgetFactory.createButton(statusPanel, "Versandbestätigung senden", "check-grey.png", this);
    deliveryConfirmButton.setDisable(true);

    TitledPane group = new TitledPane("Status", statusPanel);
    group.setPadding(new Insets(10, 10, 5, 10));
    center.getChildren().add(group);
  }

  public Order getOrder() {
    return order;
  }

  public void handle(javafx.event.ActionEvent event) {
    if(event.getSource() == contactButton) {
      MailDialog dialog = new MailDialog("Ihre Bestellung bei friemlay.de (Bestellnummer " + order.getId() + ")");
      dialog.open(event);
    }
  }

  private void createOrderItem(GridPane grid, OrderItem item, int row) {
    ImageView imageView = ResourceLoader.getWebImageView(item.getImageUrl());
    GridPane.setHalignment(imageView, HPos.RIGHT);
    GridPane.setConstraints(imageView, 0, row);
    Label productLabel = new Label(item.getProductDescription());
    GridPane.setMargin(productLabel, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(productLabel, 1, row);
    grid.getChildren().addAll(imageView, productLabel);
  }
}
