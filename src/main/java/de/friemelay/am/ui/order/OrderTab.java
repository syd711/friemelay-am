package de.friemelay.am.ui.order;

import de.friemelay.am.UIController;
import de.friemelay.am.config.Config;
import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.util.MailDialog;
import de.friemelay.am.ui.util.OrderConfirmationMailDialog;
import de.friemelay.am.ui.util.OrderDeliveryConfirmationMailDialog;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 *
 */
public class OrderTab extends Tab implements EventHandler<ActionEvent>, ChangeListener<String> {
  private Order order;

  private Button contactButton;
  private Button orderConfirmButton;
  private Button deliveryConfirmButton;
  private Button saveButton;
  private Button resetButton;
  private Button orderCancelButton;
  private final VBox orderForm = new VBox();
  private TitledPane orderItemsGroup;

  private boolean dirty;

  public OrderTab(Order order) {
    super(order.toString());
    setGraphic(ResourceLoader.getImageView(order.getStatusIcon()));
    this.order = order;
    init();
  }

  /**
   * Action handling for all buttons on the pane
   * @param event
   */
  public void handle(javafx.event.ActionEvent event) {
    if(event.getSource() == contactButton) {
      String to = order.getCustomer().getEmail().get();
      String bcc = Config.getString("mail.bcc");
      MailDialog dialog = new MailDialog("Ihre Bestellung bei friemlay.de (Bestellnummer " + order.getFormattedId() + ")", to, bcc, null, order);
      dialog.open(event);
    }
    else if(event.getSource() == orderConfirmButton) {
      String to = order.getCustomer().getEmail().get();
      String bcc = Config.getString("mail.bcc");
      List<File> attachments = Arrays.asList(new File("mail-templates/pdf/Widerrufsformular-Friemelay.pdf"));
      OrderConfirmationMailDialog dialog = new OrderConfirmationMailDialog("Bestellbestätigung/Rechnung (Bestellnummer " + order.getFormattedId() + ")", to, bcc, attachments, order);
      dialog.open(event);
    }
    else if(event.getSource() == deliveryConfirmButton) {
      String to = order.getCustomer().getEmail().get();
      String bcc = Config.getString("mail.bcc");
      OrderDeliveryConfirmationMailDialog dialog = new OrderDeliveryConfirmationMailDialog("Versandbestätigung (Bestellnummer " + order.getFormattedId() + ")", to, bcc, order);
      dialog.open(event);
    }
    else if(event.getSource() == resetButton) {
      order = DB.getOrder(order.getId());
      createOrderForms();
      setDirty(false);
    }
    else if(event.getSource() == saveButton) {
      boolean confirmed = WidgetFactory.showConfirmation("Bestellung überschreiben", "Soll die Bestellung mit den Änderungen überschrieben werden?");
      if(confirmed) {
        DB.save(order);
      }
      setDirty(!confirmed);
    }
    else if(event.getSource() == orderCancelButton) {
      boolean confirmed = WidgetFactory.showConfirmation("Bestellung stornieren?", "Soll die Bestellung storniert werden? Ein Statusaktualisierung ist danach nicht mehr möglich!");
      if(confirmed) {
        UIController.getInstance().cancelOrder(order);
        createOrderForms();
      }

    }
  }

  public void reload() {
    createOrderForms();
  }

  private void init() {
    BorderPane root = new BorderPane();

    ToolBar toolbar = new ToolBar();
    contactButton = new Button("Käufer kontaktieren", ResourceLoader.getImageView("email.png"));
    contactButton.setOnAction(this);
    saveButton = new Button("Änderungen speichern", ResourceLoader.getImageView("save.png"));
    saveButton.setOnAction(this);
    saveButton.setDisable(isReadonly() || !isDirty());
    resetButton = new Button("Änderungen zurücksetzen", ResourceLoader.getImageView("revert.png"));
    resetButton.setOnAction(this);
    resetButton.setDisable(isReadonly() || !isDirty());
    orderCancelButton = new Button("Bestellung stornieren", ResourceLoader.getImageView("remove.gif"));
    orderCancelButton.setOnAction(this);
    orderCancelButton.setDisable(isReadonly());
    toolbar.getItems().addAll(contactButton, saveButton, resetButton, orderCancelButton);
    root.setTop(toolbar);

    orderForm.setAlignment(Pos.TOP_CENTER);
    orderForm.setFillWidth(true);

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    centerScroller.setContent(orderForm);

    orderForm.setPadding(new Insets(5, 10, 5, 0));
    root.setCenter(centerScroller);

    setContent(root);

    createOrderForms();
  }

  private void createOrderForms() {
    orderForm.getChildren().clear();
    createStatusGroup(orderForm);

    GridPane orderDetailsForm = WidgetFactory.createFormGrid();
    orderForm.getStyleClass().add("root");
    int index = 0;
    WidgetFactory.addFormLabel(orderDetailsForm, "Bestellnummer:", String.valueOf(order.getId()), index++);
    WidgetFactory.addFormLabel(orderDetailsForm, "Eingang:", String.valueOf(order.getFormattedCreationDateTime()), index++);
    WidgetFactory.addBindingFormLabel(orderDetailsForm, "Name:", order.getCustomer().getAddress().getLastname(), index++, null);
    WidgetFactory.addBindingFormLabel(orderDetailsForm, "Vorname:", order.getCustomer().getAddress().getFirstname(), index++, null);
    WidgetFactory.addFormLabel(orderDetailsForm, "Preis:", index++, order.getTotalPrice(), new TotalPriceConverter(order));
    WidgetFactory.addFormLabel(orderDetailsForm, "Preis inkl. Versandkosten:", index++, order.getTotalPrice(), new TotalPriceWithShippingConverter(order));
    WidgetFactory.addFormLabel(orderDetailsForm, "Zahlungsweise:", order.getFormattedPaymentType(), index++);
    WidgetFactory.addBindingFormTextarea(orderDetailsForm, "Anmerkungen vom Kunden:", order.getComments(), index++, !isReadonly(), this);
    WidgetFactory.createSection(orderForm, orderDetailsForm, "Details der Bestellung");

    GridPane addressForm = WidgetFactory.createFormGrid();
    index = 0;
    WidgetFactory.addBindingFormTextfield(addressForm, "Name:", order.getCustomer().getAddress().getLastname(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "Vorname:", order.getCustomer().getAddress().getFirstname(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "E-Mail:", order.getCustomer().getEmail(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "Telefon:", order.getCustomer().getPhone(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "Firma:", order.getCustomer().getAddress().getCompany(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "Straße:", order.getCustomer().getAddress().getStreet(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "Adresszusatz:", order.getCustomer().getAddress().getAdditional(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "PLZ:", order.getCustomer().getAddress().getZip(), index++, !isReadonly(), this);
    WidgetFactory.addBindingFormTextfield(addressForm, "Ort:", order.getCustomer().getAddress().getCity(), index++, !isReadonly(), this);
    WidgetFactory.createSection(orderForm, addressForm, "Kundendaten", true);

    if(order.getCustomer().getBillingAddress() != null) {
      GridPane billingAddressForm = WidgetFactory.createFormGrid();
      index = 0;
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Name:", order.getCustomer().getBillingAddress().getLastname(), index++, !isReadonly(), this);
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Vorname:", order.getCustomer().getBillingAddress().getFirstname(), index++, !isReadonly(), this);
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Firma:", order.getCustomer().getBillingAddress().getCompany(), index++, !isReadonly(), this);
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Straße:", order.getCustomer().getBillingAddress().getStreet(), index++, !isReadonly(), this);
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Adresszusatz:", order.getCustomer().getBillingAddress().getAdditional(), index++, !isReadonly(), this);
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "PLZ:", order.getCustomer().getBillingAddress().getZip(), index++, !isReadonly(), this);
      WidgetFactory.addBindingFormTextfield(billingAddressForm, "Ort:", order.getCustomer().getBillingAddress().getCity(), index++, !isReadonly(), this);
      WidgetFactory.createSection(orderForm, billingAddressForm, "Rechnungsadresse", true);
    }

    createOrderItemsGroup();
  }

  private void createOrderItemsGroup() {
    if(orderItemsGroup != null) {
      orderForm.getChildren().removeAll(orderItemsGroup);
    }
    final GridPane itemsForm = WidgetFactory.createFormGrid(15, 40, 10, 10, 10, 15);
    itemsForm.setPadding(new Insets(0, 0, 0, 0));
    itemsForm.setGridLinesVisible(true);

    final HBox pg = new HBox(10);
    pg.setAlignment(Pos.CENTER);
    pg.setPadding(new Insets(10, 10, 10, 10));
    ProgressIndicator loading = new ProgressIndicator();
    pg.getChildren().addAll(loading);
    orderForm.getChildren().addAll(pg);

    Task listLoader = new Task<Boolean>() {{
      }

      @Override
      protected Boolean call() throws Exception {
        List<OrderItem> orderItems = getOrder().getOrderItems();
        int index = 0;
        for(OrderItem orderItem : orderItems) {
          createOrderItem(itemsForm, orderItem, index++);
        }
        Platform.runLater(new Runnable() {
          public void run() {
            orderForm.getChildren().remove(pg);
            orderItemsGroup = WidgetFactory.createSection(orderForm, itemsForm, "Bestellung", false);
          }
        });

        return true;
      }
    };

    Thread loadingThread = new Thread(listLoader, "item-list-loader");
    loadingThread.setDaemon(true);
    loadingThread.start();
  }

  private void createStatusGroup(Pane center) {
    HBox statusPanel = new HBox(10);
    statusPanel.getStyleClass().add("root");
    statusPanel.setAlignment(Pos.BASELINE_LEFT);
    ImageView arrow = ResourceLoader.getImageView("right-arrow.png");
    VBox imageWrapper = new VBox();
    imageWrapper.getChildren().add(arrow);

    orderConfirmButton = WidgetFactory.createButton(statusPanel, "Bestellbestätigung senden", "check-green.png", this);
    statusPanel.getChildren().addAll(imageWrapper);
    deliveryConfirmButton = WidgetFactory.createButton(statusPanel, "Versandbestätigung senden", "check-grey.png", this);

    TitledPane group = new TitledPane("Status", statusPanel);
    group.setPadding(new Insets(10, 10, 5, 10));
    center.getChildren().add(group);

    refreshOrderStatus();
  }

  public Order getOrder() {
    return order;
  }



  private void createOrderItem(GridPane grid, final OrderItem item, int row) {
    ImageView imageView = ResourceLoader.getWebImageView(item.getImageUrl());
    HBox imageWrapper = new HBox();
    GridPane.setHalignment(imageWrapper, HPos.CENTER);
    GridPane.setConstraints(imageWrapper, 0, row);
    imageWrapper.setPadding(new Insets(10, 10, 10, 10));
    imageWrapper.getChildren().add(imageView);
    grid.getChildren().addAll(imageWrapper);

    Label label = new Label(item.getProductDescription());
    GridPane.setMargin(label, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(label, 1, row);
    grid.getChildren().addAll(label);

    Spinner amount = WidgetFactory.createSpinner(1, 100, isReadonly(), item.getAmount(), this);
    GridPane.setMargin(amount, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(amount, 2, row);
    grid.getChildren().addAll(amount);

    label = new Label(String.valueOf(item.getFormattedPrice()));
    GridPane.setMargin(label, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(label, 3, row);
    grid.getChildren().addAll(label);

    Label totalPriceLabel = WidgetFactory.createLabel(String.valueOf(item.getFormattedTotalPrice()), amount.getEditor().textProperty(), new PriceStringConverter(item));
    totalPriceLabel.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        order.setTotalPrice(new TotalPriceConverter(order).fromString(null).doubleValue());
      }
    });
    GridPane.setMargin(totalPriceLabel, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(totalPriceLabel, 4, row);
    grid.getChildren().addAll(totalPriceLabel);

    Button removeButton = new Button("Löschen", ResourceLoader.getImageView("remove.gif"));
    removeButton.setDisable(isReadonly());
    removeButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        order.getOrderItems().remove(item);
        createOrderItemsGroup();
        setDirty(true);
      }
    });
    GridPane.setMargin(removeButton, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(removeButton, 5, row);
    grid.getChildren().addAll(removeButton);

  }

  private boolean isReadonly() {
    return order.getOrderStatus().get() == Order.ORDER_STATUS_CANCELED || order.getOrderStatus().get() == Order.ORDER_STATUS_DELIVERED;
  }

  public void refreshOrderStatus() {
    setGraphic(ResourceLoader.getImageView(order.getStatusIcon()));

    switch(order.getOrderStatus().getValue()) {
      case Order.ORDER_STATUS_NEW: {
        orderConfirmButton.setDisable(false);
        deliveryConfirmButton.setDisable(true);
        break;
      }
      case Order.ORDER_STATUS_CONFIRMED: {
        orderConfirmButton.setDisable(true);
        deliveryConfirmButton.setDisable(false);
        break;
      }
      case Order.ORDER_STATUS_DELIVERED: {
        orderConfirmButton.setDisable(true);
        deliveryConfirmButton.setDisable(true);
        orderCancelButton.setDisable(true);
        break;
      }
      case Order.ORDER_STATUS_CANCELED: {
        orderConfirmButton.setDisable(true);
        deliveryConfirmButton.setDisable(true);
        saveButton.setDisable(true);
        resetButton.setDisable(true);
        orderCancelButton.setDisable(true);
        orderConfirmButton.setDisable(true);
        break;
      }
    }

    if(isDirty()) {
      orderConfirmButton.setDisable(true);
      deliveryConfirmButton.setDisable(true);
    }
  }

  private void setDirty(boolean dirty) {
    this.dirty = dirty;
    saveButton.setDisable(!dirty);
    resetButton.setDisable(!dirty);
    refreshOrderStatus();
  }

  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    setDirty(true);
  }
}
