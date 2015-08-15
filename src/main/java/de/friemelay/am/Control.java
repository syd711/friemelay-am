package de.friemelay.am;

import de.friemelay.am.model.Order;
import de.friemelay.am.resources.ResourceLoader;
import de.friemelay.am.ui.OrderTabPane;
import de.friemelay.am.ui.OrderTreePane;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public class Control {
  private static Control instance = new Control();
  
  private OrderTreePane treePane;
  private OrderTabPane tabPane;
  
  public static Control getInstance() {
    return instance;    
  }
  
  public Parent init() {
    BorderPane root = new BorderPane();
    this.tabPane = new OrderTabPane();
    this.treePane = new OrderTreePane();

    SplitPane splitPane = new SplitPane();
    splitPane.getItems().addAll(treePane, tabPane);
    splitPane.setDividerPositions(0.20);
    root.setCenter(splitPane);

    final MenuBar menuBar = new MenuBar();

    Menu menu = new Menu("Verwaltung");
    MenuItem menu1 = new MenuItem("Newsletter erstellen", ResourceLoader.getImageView("email.png"));
    menu.getItems().addAll(menu1);

    Menu help = new Menu("Hilfe");
    MenuItem info = new MenuItem("Über Friemelay");
    help.getItems().addAll(info);

    menuBar.getMenus().addAll(menu, help);
    root.setTop(menuBar);

    treePane.openFirst();
    return root;
  }
  
  public void openOrder(Order order) {
    tabPane.openOrder(order);
  }

  public void selectTreeNode(Order order) {
    treePane.selectOrder(order);
  }

}
