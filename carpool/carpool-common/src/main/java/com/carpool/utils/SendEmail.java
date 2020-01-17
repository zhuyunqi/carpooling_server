package com.carpool.utils;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.carpool.entity.MessageEntity;

import java.util.Properties;

public class SendEmail {
    public void sendEmailBySmtp(MessageEntity emailSentRecord) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("redplum8008@gmail.com")); // 发送的 email 帐号 
        msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(emailSentRecord.getToMailAddress()));
        msg.setSubject(emailSentRecord.getSubject());
        msg.setContent(emailSentRecord.getContent(), "text/plain");
        
        Transport transport = session.getTransport();
        String smtpUserName = "AKIA57QKL5YA5OTXJROX"; // 带有权限的 AWS 帐号
        String smtpUserPassword = "BLg/9EhwLY19j/tmT4YmT9nTs9lJ9Qf5n+UMY9nS8PPc"; // 带有权限的 AWS 密码
        try {
            transport.connect("email-smtp." + "us-west-2" + ".amazonaws.com",
                    smtpUserName, smtpUserPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            //System.out.println("success post");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        } finally {
            transport.close();
        }
        //System.out.println("enter end");
    }
}
