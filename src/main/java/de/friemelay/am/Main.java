package de.friemelay.am;

import de.friemelay.am.db.DB;
import de.friemelay.am.images.ResourceLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 */
public class Main extends Application {
  
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    DB.connect();
    
    primaryStage.setTitle("Friemelay Auftragsverwaltung");
    primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new MainKeyEventFilter());
    primaryStage.setMinWidth(1024);
    primaryStage.setMinHeight(700);
    primaryStage.setScene(new Scene(Control.getInstance().init()));
    primaryStage.getIcons().add(new Image(ResourceLoader.getResource("favicon.png")));
    primaryStage.show();
  }
}