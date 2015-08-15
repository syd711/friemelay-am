package de.friemelay.am.mail;

/**
 * This model is used for the freemarker rendering
 */
public class MailRepresentation {
  private String to;
  private String mailText;
  private String subject;

  public MailRepresentation(String to, String subject, String mailText) {
    this.to = to;
    this.subject = subject;
    this.mailText = mailText;
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
}
