package de.friemelay.am.mail;

import de.friemelay.am.config.Config;
import de.friemelay.am.ui.util.WidgetFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 */
public class Mailer {

  private MailRepresentation model;

  public Mailer(MailRepresentation model) {
    this.model = model;
  }

  public void mail() {
    // Recipient's email ID needs to be mentioned.
    String to = model.getTo().trim();

    // Sender's email ID needs to be mentioned
    String from = Config.getString("mail.from");
    final String username = Config.getString("mail.username");
    final String password = Config.getString("mail.password");

    // Assuming you are sending email through relay.jangosmtp.net
    String host = Config.getString("mail.host");

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "25");

    // Get the Session object.
    Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });

    try {
      // Create a default MimeMessage object.
      Message message = new MimeMessage(session);

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));

      // Set To: header field of the header.
      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(to));

      if(Config.getBoolean("mail.bcc.enabled")) {
        String bcc = Config.getString("mail.bcc");
        if(!StringUtils.isEmpty(bcc)) {
          message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
        }
      }

      // Set Subject: header field
      message.setSubject(model.getSubject().trim());

      // Now set the actual message
      message.setContent(model.getMailText().trim(), "text/html");

      // Send message
      if(Config.getBoolean("mail.enabled")) {
        Transport.send(message);
      }
    } catch (MessagingException e) {
      Logger.getLogger(Mailer.class.getName()).error("Failed mailing: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to send email: " + e.getMessage(), e);
    }
  }
}
