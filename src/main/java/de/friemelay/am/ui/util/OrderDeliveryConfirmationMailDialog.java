package de.friemelay.am.ui.util;

import de.friemelay.am.UIController;
import de.friemelay.am.mail.MailRepresentation;
import de.friemelay.am.model.Order;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 *
 */
public class OrderDeliveryConfirmationMailDialog extends MailDialog {


  public OrderDeliveryConfirmationMailDialog(String subject, String to, String bcc, Order order) {
    super(subject, to, bcc, null, order);
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
  protected String getTemplateName() {
    return "delivery-confirmation-mail.ftl";
  }

  @Override
  protected void updateStatus() {
    if(getReturnCode() == 0) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          UIController.getInstance().deliveryConfirmationSent(order);
        }
      });
    }
  }
}
