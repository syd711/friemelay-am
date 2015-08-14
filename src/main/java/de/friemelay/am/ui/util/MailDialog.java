package de.friemelay.am.ui.util;

import de.friemelay.am.resources.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MailDialog implements EventHandler<ActionEvent> {

  private TextArea textArea;
  private String title;

  private Button cancleButton;
  private Button sendButton;
  private Stage stage;

  public MailDialog(String title) {
    this.title = title;
  }

  public void open(ActionEvent event) {
    stage = new Stage();
    stage.getIcons().add(new Image(ResourceLoader.getResource("email.png")));
    Parent root = createMailDialog();
    stage.setScene(new Scene(root));
    stage.setTitle(title);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(((Node) event.getSource()).getScene().getWindow());
    stage.show();

    textArea.requestFocus();
  }

  private Parent createMailDialog() {
    BorderPane root= new BorderPane();
    VBox center = new VBox();

    center.setPadding(new Insets(10, 10, 10, 10));
    root.setCenter(center);

    WebView mailHeader = new WebView();
    mailHeader.setMaxHeight(170);
    WebEngine webEngine = mailHeader.getEngine();
    webEngine.load(ResourceLoader.getResource("template-header.html"));
    mailHeader.getStyleClass().add("email");

    WebView mailFooter = new WebView();
    mailFooter.setMaxHeight(150);
    webEngine = mailFooter.getEngine();
    webEngine.load(ResourceLoader.getResource("template-footer.html"));
    mailFooter.getStyleClass().add("email");

    textArea = new TextArea();
    textArea.getStyleClass().add("email-text");
    textArea.setPadding(new Insets(10, 10, 10, 10));

    center.getChildren().addAll(mailHeader, textArea, mailFooter);

    HBox footer = new HBox(5);
    footer.setAlignment(Pos.BASELINE_RIGHT);
    footer.setPadding(new Insets(10, 10, 10, 10));
    sendButton = new Button("E-Mail senden", ResourceLoader.getImageView("email.png"));
    sendButton.setOnAction(this);
    cancleButton = new Button("Abbrechen");
    cancleButton.setOnAction(this);
    footer.getChildren().addAll(sendButton, cancleButton);
    root.setBottom(footer);
    return root;
  }

  public void handle(ActionEvent event) {
    if(event.getSource() == cancleButton) {
      stage.close();
    }
  }
}
