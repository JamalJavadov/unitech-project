package com.example.unitechproject.exception;

public class TokenNotFound extends RuntimeException{
    public TokenNotFound(String message){
        super(message);
    }
}
