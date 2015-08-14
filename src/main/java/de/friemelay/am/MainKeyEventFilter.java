package de.friemelay.am;

import javafx.scene.input.KeyEvent;

/**
 *
 */
public class MainKeyEventFilter implements javafx.event.EventHandler<KeyEvent> {


  public void handle(KeyEvent event) {
    System.out.println(event);
  }
}
