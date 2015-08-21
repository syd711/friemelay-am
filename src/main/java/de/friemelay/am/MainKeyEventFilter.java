package de.friemelay.am;

import de.friemelay.am.ui.util.WidgetFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 */
public class MainKeyEventFilter implements javafx.event.EventHandler<KeyEvent> {


  public void handle(KeyEvent event) {
    if(event.getCode() == KeyCode.O && event.isControlDown() && event.isAltDown()) {
      boolean confirmed = WidgetFactory.showConfirmation("Bestellen zurücksetzen?", "Dieser Shortcut stellt den ursprünglichen Bestellstatus wieder hier und ist nur " +
          "für Entwicklungszwecke gedacht. Fortfahren?");
      if(confirmed) {
        UIController.getInstance().resetSelectedOrder();
      }
    }
  }
}
