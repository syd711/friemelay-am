package de.friemelay.am;

import de.friemelay.am.ui.util.TransitionUtil;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

/**
 * Clean up some status
 */
public class UITaskThread extends Thread {

  private boolean running = true;
  private boolean dirty = false;

  @Override
  public void run() {
    while(running) {
      try {
        Thread.sleep(2000);

        if(dirty) {
          Thread.sleep(5000);
          // UI updaten
          Platform.runLater(new Runnable() {
            public void run() {
              final Label statusMessage = UIController.getInstance().getStatusMessage();
              FadeTransition outFader = TransitionUtil.createOutFader(statusMessage, 1000);
              outFader.setOnFinished(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                  UIController.getInstance().setStatusMessage("");
                  statusMessage.setOpacity(1);
                }
              });
              outFader.play();
            }
          });
          dirty = false;
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
}
