package com.example.unitechproject.exception;

public class VerifyCodeFailedException extends RuntimeException{
    public VerifyCodeFailedException(String message){
        super(message);
    }
}
