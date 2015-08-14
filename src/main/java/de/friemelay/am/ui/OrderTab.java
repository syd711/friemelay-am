package de.friemelay.am.ui;

import de.friemelay.am.model.Order;
import de.friemelay.am.ui.util.WidgetFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;


/**
 *
 */
public class OrderTab extends Tab {
  private Order order;

  public OrderTab(Order order) {
    super(order.toString());
    this.order = order;
    init();
  }

  private void init() {
    BorderPane root = new BorderPane();
    setContent(root);

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    ColumnConstraints colInfo2 = new ColumnConstraints();
    colInfo2.setMinWidth(100);
    ColumnConstraints colInfo3 = new ColumnConstraints();
    colInfo3.setPercentWidth(80);
    grid.getColumnConstraints().add(colInfo2); //25 percent
    grid.getColumnConstraints().add(colInfo3); //50 percent

    int index = 0;
    WidgetFactory.addFormLabel(grid, "Bestellnummer:", String.valueOf(order.getId()), index++);
    WidgetFactory.addFormTextfield(grid, "Name:", order.getCustomer().getAddress().getLastname(), index++);
    WidgetFactory.addFormTextfield(grid, "Vorname:", order.getCustomer().getAddress().getFirstname(), index++);
    WidgetFactory.addFormTextfield(grid, "Firma:", order.getCustomer().getAddress().getCompany(), index++);
    WidgetFactory.addFormTextfield(grid, "Straße:", order.getCustomer().getAddress().getCity(), index++);
    WidgetFactory.addFormTextfield(grid, "Adresszusatz:", order.getCustomer().getAddress().getAdditional(), index++);
    WidgetFactory.addFormTextfield(grid, "PLZ:", order.getCustomer().getAddress().getZip(), index++);
    WidgetFactory.addFormTextfield(grid, "Ort:", order.getCustomer().getAddress().getCity(), index++);
    root.setCenter(grid);
  }

  public Order getOrder() {
    return order;
  }
}
