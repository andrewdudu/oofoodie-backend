package com.oofoodie.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private JavaMailSender javaMailSender;

    @Value("${frontend.url}/reset-password")
    private String url;

    public MailService(JavaMailSender javaMailSender) { this.javaMailSender = javaMailSender; }

    public Boolean sendForgotPasswordMail(String email, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(email);
        msg.setSubject("Reset Password");
        msg.setText(url + "?token=" + token);

        javaMailSender.send(msg);

        return true;
    }

    public Boolean sendEmail(String email, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(email);
        msg.setSubject("ooFoodie Split Bill");
        msg.setText(message);

        javaMailSender.send(msg);

        return true;
    }
}
