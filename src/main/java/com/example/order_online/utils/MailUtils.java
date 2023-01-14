package com.example.order_online.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author 16537
 * @Classname MailUtils
 * @Description
 * @Version 1.0.0
 * @Date 2022/10/6 12:48
 */
@Component
@Scope("prototype")
public class MailUtils {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    @Async("mailPool")
    public void sendEmail(String subject,String content,String ...to){

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(username);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);
            mimeMessageHelper.setSentDate(new Date());
            mimeMessageHelper.setTo(to);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败",e);
        }
    }
}
