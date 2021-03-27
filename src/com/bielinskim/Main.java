package com.bielinskim;

import java.io.IOException;
import java.util.*;
import javax.jms.Message;
import javax.jms.Session;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Main {

    public static void main(String[] args) {
        try {
            //wysyłanie wiadomości bez autoryzacji
            send("localhost", 25, "mateusz100022@gmail.com", "mateusz100022@gmail.com", "Gra kółko i krzyżyk", "Start Gry");
        } catch (AddressException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        try {
            String w = getMail("localhost", "test2@localhost", "test123", "pop3");
            System.out.println(w);
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    public static void send(String smtpHost, int smtpPort, String from, String to, String subject,
                            String content) throws AddressException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "" + smtpPort);
        Session session = Session.getDefaultInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(content);
        Transport.send(msg);
    }

    public static String getMail(String host, String username, String password, String provider) throws NoSuchProviderException, MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore(provider);
        store.connect(host, username, password);
        Folder inbox = store.getFolder("INBOX");
        if (inbox == null) {
            System.out.println("No INBOX");
            System.exit(1);
        }
        inbox.open(Folder.READ_ONLY);
        Message[] messages = inbox.getMessages();
        for (int i = 0; i < messages.length; i++) {
            System.out.println("Message " + (i + 1));
            messages[i].writeTo(System.out);
        }
        inbox.close(false);
        store.close();
        if (messages.length>0 && messages[0].getContent() instanceof String) {
            return (String) messages[0].getContent();
        }
        return null;
    }


}
