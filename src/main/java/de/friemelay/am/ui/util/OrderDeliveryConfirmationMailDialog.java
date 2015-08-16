package de.friemelay.am.ui.util;

import de.friemelay.am.UIController;
import de.friemelay.am.mail.MailRepresentation;
import de.friemelay.am.model.Order;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 *
 */
public class OrderDeliveryConfirmationMailDialog extends MailDialog {

  private Order order;

  public OrderDeliveryConfirmationMailDialog(String subject, String to, String bcc, Order order) {
    super(subject, to, bcc);
    this.order = order;
  }

  @Override
  protected void createMailPane(VBox mailPane) {
    MailRepresentation model = getModel();
    updateModel(model);

    WebView mailHeader = WidgetFactory.createTemplateWebView(model, getTemplateName());
    mailHeader.setMaxHeight(710);
    mailHeader.getStyleClass().add("email");
    mailPane.getChildren().addAll(mailHeader);

    model.setMailText((String) mailHeader.getUserData());
  }

  @Override
  protected void updateModel(MailRepresentation model) {
    model.setName(order.getCustomer().getAddress().getFirstname().get() + " " + order.getCustomer().getAddress().getLastname().get());
    model.setOrderId(String.valueOf(order.getId()));
    model.setOrder(order);
  }

  @Override
  protected String getTemplateName() {
    return "delivery-confirmation-mail.ftl";
  }

  @Override
  protected void updateStatus() {
    if(getReturnCode() == 0) {
      UIController.getInstance().deliveryConfirmationSent(order);
    }
  }
}
