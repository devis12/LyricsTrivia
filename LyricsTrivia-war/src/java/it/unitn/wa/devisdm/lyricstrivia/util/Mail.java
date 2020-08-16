/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.wa.devisdm.lyricstrivia.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author devis
 */

public class Mail {

    private static final String username = "devisdalmoro@gmail.com";
    private static final String password = "itnivowvzdrwurhv"; // string of 16 charachters used as secure key for this application
    private static final String host = "smtp.gmail.com";
    private static final String port = "465";
    private String receiver;
    private String object;
    private String text;

    /**
     * *
     * Function to send a mail using google mail
     *
     * @param receiver : receiver mail
     * @param object : object of the message
     * @param text : text of the message
     * @throws it.unitn.wa.devisdm.lyricstrivia.util.MailException
     */
    public Mail(String receiver, String object, String text) throws MailException {
        if(!isValidEmailAddress(receiver))
            throw new MailException("Destination mail is unvalid");
        this.receiver = receiver;
        this.object = object;
        this.text = text;
    }
    
    /**
     * *
     * Funtcion to check if an email address is valid
     * @param email : email to check
     * @ret true if email is valide, false if not
     */
    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    /**
     * *
     * Function to call to send the email created with preconfigured settings
     */
    public void send() {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.port", port);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.debug", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
            msg.setSubject(object);
            msg.setContent(text, "text/html;charset=UTF-8");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException me) {

            me.printStackTrace(System.err);
        }

    }
    
}
