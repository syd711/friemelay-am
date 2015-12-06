package de.friemelay.am.mail;

import de.friemelay.am.config.Config;
import org.apache.commons.lang.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class Mailer {

  private MailRepresentation model;

  public Mailer(MailRepresentation model) {
    this.model = model;
  }

  public void mail() throws MessagingException {
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
    props.put("mail.mime.charset", "utf8");

    // Get the Session object.
    Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });
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

    //build message part
    BodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setContent(model.getMailText().trim(), "text/html; charset=UTF-8");

    //add text part
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);


    List<File> attachments = model.getAttachments();
    if(attachments != null && !attachments.isEmpty()) {
      for(File attachment : attachments) {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);
        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(attachment.getName());
        multipart.addBodyPart(mimeBodyPart);
      }
    }

    // Send the complete message parts
    message.setContent(multipart);

    // Send message
    if(Config.getBoolean("mail.enabled")) {
      Transport.send(message);
    }
  }
}
