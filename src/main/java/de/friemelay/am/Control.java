package de.friemelay.am;

import de.friemelay.am.db.DB;
import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import de.friemelay.am.ui.OrderTabPane;
import de.friemelay.am.ui.OrderTreePane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
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
    splitPane.setDividerPositions(0.05);
    root.setCenter(splitPane);

    final MenuBar menuBar = new MenuBar();

    Menu menu = new Menu("Submenu 1");
    // Options->Submenu 2 submenu
    MenuItem menu1 = new MenuItem("Item 1");
    MenuItem menu2 = new MenuItem("Item 2");
    menu.getItems().addAll(menu1, menu2);

    menuBar.getMenus().addAll(menu);
    root.setTop(menuBar);
    return root;
  }
  
  public void openOrder(Order order) {
    tabPane.openOrder(order);
  }
}
