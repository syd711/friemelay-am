package de.friemelay.am.ui.catalog;

import de.friemelay.am.resources.ResourceLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.log4j.Logger;

/**
 * Created by Matthias on 11.09.2015.
 */
public class PreviewPanel extends BorderPane {

  private boolean hidden = true;
  private SplitPane splitPane;
  private static double position = 0.95;
  private boolean listenerAdded = false;
  private WebEngine webEngine;
  private boolean needsReload = true;
  private boolean isLoading = false;
  private int triggerCount = 0;

  public PreviewPanel(SplitPane splitPane) {
    init();
    this.splitPane = splitPane;
    this.splitPane.setDividerPosition(0, position);
  }

  private void init() {
    ToolBar toolbar = new ToolBar();
    Button showHideButton = new Button("", ResourceLoader.getImageView("plus.png"));
    showHideButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if(hidden) {
          splitPane.setDividerPosition(0, 0.5);
        }
        else {
          splitPane.setDividerPosition(0, position);
        }
      }
    });
    showHideButton.setTooltip(new Tooltip("Vorschau einblenden"));
    toolbar.getItems().addAll(showHideButton);

    setTop(toolbar);

    setCenter(createPreview());

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        checkListener();
      }
    });
  }

  private void checkListener() {
    if(!listenerAdded) {
      listenerAdded = true;
      splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
          position = (double) newValue;
          hidden = position < 0.5;
          if(!hidden) {
            refresh();
          }
        }
      });
    }
  }

  private Pane createPreview() {
    BorderPane center = new BorderPane();
    WebView webView = new WebView();
    webEngine = webView.getEngine();
    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
      @Override
      public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
        if(Worker.State.SUCCEEDED == newValue) {
          System.out.println(newValue);
          isLoading = false;
          if(needsReload) {
            needsReload = false;
            webEngine.load("http://www.friemelay.de/index.php");
          }
          else {
            isLoading = false;
          }
        }
      }
    });
    center.setCenter(webView);
    return center;
  }

  public void refresh() {
    if(!hidden) {
      if(!isLoading) {
        isLoading = true;
        triggerCount++;
        String url = "http://www.friemelay.de/index.php#" + triggerCount;
        Logger.getLogger(getClass()).info("Refreshing preview: " + url);
        webEngine.load(url);
      }
      else {
        needsReload = true;
      }
    }

  }
}
