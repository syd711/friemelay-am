package de.friemelay.am.mail;

import de.friemelay.am.config.Config;
import de.friemelay.am.model.Address;
import de.friemelay.am.model.Order;

import java.io.File;
import java.util.List;

/**
 * This model is used for the freemarker rendering
 */
public class MailRepresentation {
  private String to;
  private String mailText;
  private String subject;
  private List<File> attachments;

  private Order order;
  private Address address;

  private String iban;
  private String blz;
  private String ustid;

  public MailRepresentation(String to, String subject, Order order) {
    this.to = to;
    this.subject = subject;
    this.order = order;
    address = order.getCustomer().getAddress();

    this.iban = Config.getString("iban.id");
    this.blz = Config.getString("blz.id");
    this.ustid = Config.getString("ust.id.nr");
  }

  public String getName() {
    return order.getCustomer().getAddress().getFirstname().get() + " " + order.getCustomer().getAddress().getLastname().get();
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

  public String getTo() {
    return to;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Address getAddress() {
    return address;
  }

  public String getUstid() {
    return ustid;
  }

  public String getBlz() {
    return blz;
  }


  public String getIban() {
    return iban;
  }

  public List<File> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<File> attachments) {
    this.attachments = attachments;
  }
}
