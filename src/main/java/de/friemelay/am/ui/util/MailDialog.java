package de.friemelay.am.ui.util;

import de.friemelay.am.UIController;
import de.friemelay.am.mail.MailRepresentation;
import de.friemelay.am.mail.Mailer;
import de.friemelay.am.mail.TemplateService;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MailDialog implements EventHandler<ActionEvent> {

  private TextArea textArea;
  private Button cancleButton;
  private Button sendButton;
  private Stage stage;

  private TextField subjectText;
  private TextField toText;

  private String subject;
  private String to;
  private String bcc;

  private int returnCode = -1;

  public MailDialog(String subject, String to, String bcc) {
    this.subject = subject;
    this.to = to;
    this.bcc = bcc;
  }

  public void open(ActionEvent event) {
    stage = new Stage();
    stage.getIcons().add(new Image(ResourceLoader.getResource("email.png")));
    Parent root = createMailDialog();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle(subject);
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(((Node) event.getSource()).getScene().getWindow());
    stage.show();
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent t) {
        KeyCode key = t.getCode();
        if(key == KeyCode.ESCAPE) {
          stage.close();
        }
      }
    });

    if(textArea != null) {
      textArea.requestFocus();
    }
  }

  private Parent createMailDialog() {
    BorderPane root= new BorderPane();
    VBox center = new VBox();

    center.setPadding(new Insets(5, 5, 5, 5));
    root.setCenter(center);

    GridPane mailForm = WidgetFactory.createFormGrid(6, 94);
    int index = 0;
    subjectText = WidgetFactory.addFormTextfield(mailForm, "Betreff:", this.subject, index++);
    toText = WidgetFactory.addFormTextfield(mailForm, "An:", to, index++);
    WidgetFactory.addFormTextfield(mailForm, "Kopie an:", bcc, index++, false);
    center.getChildren().add(mailForm);

    VBox mailPane = new VBox();
    mailPane.setStyle("-fx-border-color:#ccc;-fx-border-width:2px;-fx-border-radius: 5 5 5 5;");
    mailPane.getStyleClass().add("email");
    WidgetFactory.addFormPane(mailForm, "E-Mail:", mailPane, index++);

    createMailPane(mailPane);

    HBox footer = new HBox(5);
    footer.setAlignment(Pos.BASELINE_RIGHT);
    footer.setPadding(new Insets(0, 20, 15, 0));
    sendButton = new Button("E-Mail senden", ResourceLoader.getImageView("email.png"));
    sendButton.setOnAction(this);
    cancleButton = new Button("Abbrechen");
    cancleButton.setOnAction(this);
    footer.getChildren().addAll(sendButton, cancleButton);
    root.setBottom(footer);
    return root;
  }

  protected void createMailPane(VBox mailPane) {
    WebView mailHeader = new WebView();
    mailHeader.setMaxHeight(170);
    WebEngine webEngine = mailHeader.getEngine();
    webEngine.load(ResourceLoader.getTemplate("contact-header.html"));
    mailHeader.getStyleClass().add("email");

    WebView mailFooter = new WebView();
    mailFooter.setMaxHeight(120);
    webEngine = mailFooter.getEngine();
    webEngine.load(ResourceLoader.getTemplate("contact-footer.html"));
    mailFooter.getStyleClass().add("email");

    textArea = new TextArea();
    textArea.getStyleClass().add("email-text");
    textArea.setPadding(new Insets(5, 5, 5, 5));

    mailPane.getChildren().addAll(mailHeader, textArea, mailFooter);
  }

  public void handle(ActionEvent event) {
    if(event.getSource() == cancleButton) {
      stage.close();
    }
    else if(event.getSource() == sendButton) {
      MailRepresentation model = getModel();
      updateModel(model);

      String mailText = TemplateService.getTemplateSet().renderTemplate(getTemplateName(), model);
      model.setMailText(mailText);

      Mailer mailer = new Mailer(model);
      mailer.mail();
      returnCode = 0;
      updateStatus();
      stage.close();
    }
  }

  protected void updateStatus() {
    if(getReturnCode() == 0) {
      UIController.getInstance().setStatusMessage("Kontaktmail versendet");
    }
  }

  protected MailRepresentation getModel() {
    return new MailRepresentation(toText.getText(), subjectText.getText());
  }

  protected void updateModel(MailRepresentation model) {
    model.setMailText(textArea.getText().trim());
  }

  protected String getTemplateName() {
    return "contact-mail.ftl";
  }

  public int getReturnCode() {
    return returnCode;
  }
}
