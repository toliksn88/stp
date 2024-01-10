package com.innowise.secret_santa.service.email_services;

import com.innowise.secret_santa.exception.ErrorSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            sender.send(message);
        } catch (Exception ex) {
            throw new ErrorSendEmailException("Error send Email: " + ex.getMessage());
        }
    }
}
