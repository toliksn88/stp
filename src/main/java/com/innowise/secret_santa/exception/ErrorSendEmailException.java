package com.innowise.secret_santa.exception;

public class ErrorSendEmailException extends RuntimeException {
    public ErrorSendEmailException(String message) {
        super(message);
    }
}
