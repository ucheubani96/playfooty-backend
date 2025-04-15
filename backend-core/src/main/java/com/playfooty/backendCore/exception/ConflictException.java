package com.playfooty.backendCore.exception;

public class ConflictException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public ConflictException(String message) {
        super(message);
    }
}
