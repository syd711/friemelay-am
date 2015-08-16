package de.friemelay.am.ui.util;

import de.friemelay.am.UIController;
import de.friemelay.am.mail.MailRepresentation;
import de.friemelay.am.mail.TemplateService;
import de.friemelay.am.model.Order;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 */
public class OrderConfirmationMailDialog extends MailDialog {

  private Order order;

  public OrderConfirmationMailDialog(String subject, String to, String bcc, Order order) {
    super(subject, to, bcc);
    this.order = order;
  }

  @Override
  protected void createMailPane(VBox mailPane) {
    WebView mailHeader = new WebView();
    mailHeader.setMaxHeight(310);
    mailHeader.getStyleClass().add("email");

    WebEngine webEngine = mailHeader.getEngine();
    MailRepresentation model = getModel();
    updateModel(model);
    String mailText = TemplateService.getTemplateSet().renderTemplate(getTemplateName(), model);
    model.setMailText(mailText);
    webEngine.loadContent(mailText);

    mailPane.getChildren().addAll(mailHeader);
  }

  @Override
  protected void updateModel(MailRepresentation model) {
    model.setName(order.getCustomer().getAddress().getFirstname().get() + " " + order.getCustomer().getAddress().getLastname().get());
    model.setOrderId(String.valueOf(order.getId()));
  }

  @Override
  protected String getTemplateName() {
    return "order-confirmation-mail.ftl";
  }

  @Override
  protected void updateStatus() {
    if(getReturnCode() == 0) {
      UIController.getInstance().orderConfirmationSend(order);
    }
  }
}
