package com.app.estockcard.controller.mail;

import java.util.Properties;

public class SendEmailTLS {

//    public void sendMail(){
//        final String username = "bitdevcontact@gmail.com";
////      final String password = "uadjcwgzraxterzv";
//        final String password = "";
//
//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS
//
//        Session session = Session.getInstance(prop,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("bitdevcontact@gmail.com"));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse("nico.exiglosi@gmail.com")
//            );
//
//            message.setSubject("Testing Gmail TLS");
//            message.setText("Dear Mail Crawler,"
//                    + "\n\n Please do not spam my email!");
//
//            Multipart multipart = new MimeMultipart();
//            BodyPart htmlPart = new MimeBodyPart();
//            String content = "<center><H1>This is the image</H1><br>"+
//                    "<H4>From nico</H2></center>";
//            htmlPart.setContent(content,"text/html; charset=utf-8");
//            htmlPart.setDisposition(BodyPart.INLINE);
//            multipart.addBodyPart(htmlPart);
//
//            message.setContent(multipart);
//
//
//            Transport.send(message);
//
//            System.out.println("Done");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }


}
