package com.playfooty.backendCore.exception;

public class ForbiddenException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("User access forbidden");
    }
}
