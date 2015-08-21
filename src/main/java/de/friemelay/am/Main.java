package de.friemelay.am;

import de.friemelay.am.db.DB;
import de.friemelay.am.resources.ResourceLoader;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 */
public class Main extends Application {
  
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    PropertyConfigurator.configure("conf/log4j.properties");

    DB.connect();
    
    primaryStage.setTitle("Friemelay Auftragsverwaltung");
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setMaxWidth(primaryScreenBounds.getWidth());
    primaryStage.setMaxHeight(primaryScreenBounds.getHeight());
    primaryStage.setHeight(700);
    primaryStage.setWidth(1200);
    primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new MainKeyEventFilter());

//    primaryStage.setWidth(primaryScreenBounds.getWidth());
//    primaryStage.setHeight(primaryScreenBounds.getHeight());
    Scene scene = new Scene(UIController.getInstance().init());
    scene.getStylesheets().add(ResourceLoader.getResource("theme.css"));
    primaryStage.setScene(scene);
    UIController.getInstance().setStage(primaryStage);
    primaryStage.getIcons().add(new Image(ResourceLoader.getResource("favicon.png")));
    primaryStage.show();
    UIController.getInstance().reloadOrders();
  }
}
