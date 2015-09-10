package de.friemelay.am;

import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.catalog.CatalogTreePane;
import de.friemelay.am.ui.ItemsTabPane;
import de.friemelay.am.ui.order.OrderTreePane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 */
public class MainPanel extends BorderPane {

  private OrderTreePane orderTreePane;
  private CatalogTreePane catalogTreePane;
  private ItemsTabPane tabPane;

  private Label statusMessage = new Label("");

  private Label infoMessage = new Label("");

  private TitledPane catalogTreeTitledPane;
  public MainPanel() {
    this.tabPane = new ItemsTabPane();
    this.orderTreePane = new OrderTreePane();
    this.catalogTreePane = new CatalogTreePane();

    SplitPane splitPane = new SplitPane();
    TitledPane ordersTreeTitledPane = new TitledPane();
    ordersTreeTitledPane.setText("Bestellungen");
    ordersTreeTitledPane.setContent(orderTreePane);

    catalogTreeTitledPane = new TitledPane();
    catalogTreeTitledPane.setText("Katalog");
    catalogTreeTitledPane.setContent(catalogTreePane);

    final Accordion accordion = new Accordion();
    accordion.setMaxWidth(600);
    accordion.setMinWidth(400);
    accordion.getPanes().addAll(ordersTreeTitledPane, catalogTreeTitledPane);

    splitPane.getItems().addAll(accordion, tabPane);
    setCenter(splitPane);

    final MenuBar menuBar = new MenuBar();

    Menu menu = new Menu("Verwaltung");
    MenuItem menu1 = new MenuItem("Newsletter erstellen (TODO)", ResourceLoader.getImageView("email.png"));
    menu.getItems().addAll(menu1);

    Menu help = new Menu("Hilfe");
    MenuItem info = new MenuItem("Über Friemelay");
    help.getItems().addAll(info);

    menuBar.getMenus().addAll(menu);
    setTop(menuBar);

    BorderPane footer = new BorderPane();
    footer.setStyle("-fx-background-color:#DDD;");

    HBox statusBox = new HBox();
    statusBox.setAlignment(Pos.BASELINE_RIGHT);
    statusBox.getChildren().addAll(statusMessage);
    statusMessage.setPadding(new Insets(0, 5, 2, 0));

    HBox infoBox = new HBox();
    infoBox.setAlignment(Pos.BASELINE_LEFT);
    infoBox.getChildren().addAll(infoMessage);
    infoMessage.setStyle("-fx-font-weight: bold;");
    infoMessage.setPadding(new Insets(0, 0, 2, 5));

    footer.setLeft(infoBox);
    footer.setCenter(statusBox);
    setBottom(footer);

    orderTreePane.openFirst();
  }

  public OrderTreePane getOrderTreePane() {
    return orderTreePane;
  }

  public ItemsTabPane getTabPane() {
    return tabPane;
  }

  public Label getStatusMessage() {
    return statusMessage;
  }

  public CatalogTreePane getCatalogTreePane() {
    return catalogTreePane;
  }

  public void expandCatalog() {
    catalogTreeTitledPane.setExpanded(true);
  }

  public Label getInfoMessage() {
    return infoMessage;
  }
}
