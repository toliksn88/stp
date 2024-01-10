package com.innowise.secret_santa.exception;

public class NoAccessException extends RuntimeException {
    public NoAccessException(String s) {
        super(s);
    }
}