package com.innowise.secret_santa.service.password_service;

public interface PasswordService {
    void comparePasswords(String currentPassword, String validPassword);

    String encodePassword(String password);
}
