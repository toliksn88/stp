package com.innowise.secret_santa.exception;

public class UnknownMessageException extends RuntimeException {
    public UnknownMessageException(String message) {
        super(message);
    }
}
