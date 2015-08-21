package de.friemelay.am.ui.util;

import de.friemelay.am.UIController;
import de.friemelay.am.mail.MailRepresentation;
import de.friemelay.am.mail.TemplateService;
import de.friemelay.am.model.Order;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.List;

/**
 *
 */
public class OrderConfirmationMailDialog extends MailDialog {

  public OrderConfirmationMailDialog(String subject, String to, String bcc, List<File> attachments, Order order) {
    super(subject, to, bcc, attachments, order);
  }

  @Override
  protected void createMailPane(VBox mailPane) {
    WebView mailHeader = new WebView();
    mailHeader.setMaxHeight(510);
    mailHeader.getStyleClass().add("email");

    WebEngine webEngine = mailHeader.getEngine();
    MailRepresentation model = getModel();
    String mailText = TemplateService.getTemplateSet().renderTemplate(getTemplateName(), model);
    model.setMailText(mailText);
    webEngine.loadContent(mailText);

    mailPane.getChildren().addAll(mailHeader);
  }

  @Override
  protected String getTemplateName() {
    return "order-confirmation-mail.ftl";
  }

  @Override
  protected void updateStatus() {
    if(getReturnCode() == 0) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          UIController.getInstance().orderConfirmationSent(order);
        }
      });
    }
  }
}
