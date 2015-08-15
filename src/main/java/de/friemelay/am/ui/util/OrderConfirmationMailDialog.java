package de.friemelay.am.ui.util;

import de.friemelay.am.model.Order;
import javafx.scene.layout.VBox;

/**
 *
 */
public class OrderConfirmationMailDialog extends MailDialog {


  public OrderConfirmationMailDialog(String subject, String to, String bcc, Order order) {
    super(subject, to, bcc);
  }

  @Override
  protected void createMailPane(VBox mailPane) {
    super.createMailPane(mailPane);
  }

  @Override
  protected String getTemplateName() {
    return "order-confirmation.ftl";
  }
}
