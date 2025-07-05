package com.whitestone.hrms.controller;
 
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
 
public class gmail {
 
    public static void main(String[] args) {
        // Custom mail server details
        final String host = "mail.whitestones.co.in"; // Replace with your mail server host
        final String username = "onboarding@whitestones.co.in"; // Replace with your email
        final String password = "Whitestone#321"; // Replace with your email password
        final int port = 465; // Common SMTP port for SSL
 
        // Email details
        final String toEmail = "britto.anthonisami@whitestones.in"; // Replace with recipient email
        final String subject = "Test Email from Custom Mail Server";
        final String body = "Hello, this is a test email triggered from Java using the custom mail server.";
 
        // Set properties for the mail session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // Enable SSL encryption
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
 
        // Authenticate the session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
 
        try {
            // Create a MIME email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Sender's email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Recipient's email
            message.setSubject(subject);
            message.setText(body);
 
            // Send the email
            Transport.send(message);
 
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.err.println("Failed to send email.");
            e.printStackTrace();
        }
    }
}