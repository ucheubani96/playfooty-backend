package com.playfooty.backendCore.exception;

public class BadRequestException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public BadRequestException(String message) {
        super(message);
    }
}
