package com.innowise.secret_santa.service.password_service;

import com.innowise.secret_santa.exception.IncorrectDataException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService{

    private final PasswordEncoder encoder;

    public PasswordServiceImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void comparePasswords(String currentPassword, String validPassword) {
        if (!encoder.matches(currentPassword, validPassword)) {
            throw new IncorrectDataException("Password incorrect, please write it again");
        }
    }

    @Override
    public String encodePassword(String password){
        return encoder.encode(password);
    }
}
