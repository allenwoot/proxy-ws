package com.jab.proxy.web_service.utilities;

import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jab.proxy.web_service.beans.Intent;
import com.jab.proxy.web_service.beans.ProxyRequest;
import com.jab.proxy.web_service.beans.RestaurantResRequest;
import com.jab.proxy.web_service.beans.User;
import com.sun.mail.smtp.SMTPTransport;

public class GmailNotifier {
    private static List<String> recipients = Arrays.asList("allen@heyproxy.io", "brian@heyproxy.io", "justin@heyproxy.io");

    public static boolean notifyRegisteredUser(final User user) {
        final String subject = "New proxy user registration: " + user.getEmail();
        final StringBuilder body = new StringBuilder();
        body.append("First name: ").append(user.getFirstName()).append("\n");
        body.append("Last name: ").append(user.getLastName()).append("\n");
        body.append("Email: ").append(user.getEmail()).append("\n");
        body.append("User ID: ").append(user.getId()).append("\n");
        body.append("Number: ").append(user.getNumber());
        try {
            send(recipients, subject, body.toString());
            return true;
        } catch (final MessagingException e) {
            return false;
        }
    }

    public static boolean notifySubmittedRequest(final User user, final ProxyRequest request) {
        final String subject = formatNewRequestSubject(request);
        final String body = formatNewRequestBody(user, request);
        try {
            send(recipients, subject, body);
            return true;
        } catch (final MessagingException e) {
            return false;
        }
    }

    private static String formatNewRequestBody(final User user, final ProxyRequest request) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Requestor name: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n");
        sb.append("Requestor ID: ").append(user.getId()).append("\n");
        sb.append("Requestor email: ").append(user.getEmail()).append("\n\n");
        sb.append("Request ID: ").append(request.getId()).append("\n");
        sb.append("Request intent: ").append(request.getIntent().name()).append("\n");
        sb.append("Request status: ").append(request.getStatus().name()).append("\n");

        if (!Intent.RESTAURANT_RESERVATION.equals(request.getIntent())) {
            return sb.toString();
        }

        final RestaurantResRequest restaurantResRequest = (RestaurantResRequest) request;
        sb.append("Restaurant: ").append(restaurantResRequest.getRestaurant()).append("\n");
        sb.append("Party size: ").append(restaurantResRequest.getPartySize()).append("\n");
        sb.append("Datetime: ").append(restaurantResRequest.getDateTime()).append("\n");

        return sb.toString();
    }

    private static String formatNewRequestSubject(final ProxyRequest request) {
        final StringBuilder sb = new StringBuilder();
        sb.append("New proxy request: ").append(request.getId());
        return sb.toString();
    }

    /**
     * Send email using GMail SMTP server.
     *
     * @param recipientEmail TO recipient
     * @param title title of the message
     * @param message message to be sent
     * @throws MessagingException if the connection is dead or not in the connected state or if the message is not a MimeMessage
     */
    private static void send(final List<String> recipientEmails, final String title, final String message) throws MessagingException, javax.mail.MessagingException {
        final String username = "proxynotifications";
        final String password = "ProxyNotifications1";

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        final Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        /*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
        */
        props.put("mail.smtps.quitwait", "false");

        final Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(username + "@gmail.com"));
        final List<InternetAddress> internetAddresses = new ArrayList<InternetAddress>();
        for (final String email : recipientEmails) {
            internetAddresses.add(new InternetAddress(email));
        }
        msg.setRecipients(Message.RecipientType.TO, internetAddresses.toArray(new Address[internetAddresses.size()]));

        msg.setSubject(title);
        msg.setText(message, "utf-8");
        msg.setSentDate(new Date());

        final SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }

    private GmailNotifier() {
    }
}
