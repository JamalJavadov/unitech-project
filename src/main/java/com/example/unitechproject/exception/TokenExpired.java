package com.example.unitechproject.exception;

public class TokenExpired extends RuntimeException {
    public TokenExpired(String message) {
        super(message);
    }
}
