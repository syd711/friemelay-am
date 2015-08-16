package de.friemelay.am.mail;

import de.friemelay.am.model.Order;

/**
 * This model is used for the freemarker rendering
 */
public class MailRepresentation {
  private String to;
  private String mailText;
  private String subject;

  private String name;
  private String orderId;
  private Order order;

  public MailRepresentation(String to, String subject) {
    this.to = to;
    this.subject = subject;
  }


  public String getMailText() {
    return mailText;
  }

  public void setMailText(String mailText) {
    this.mailText = mailText;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }
}
