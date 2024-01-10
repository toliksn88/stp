package com.innowise.secret_santa.service.email_services;

public interface EmailService {

    void sendMail(String to, String subject, String text);
}
