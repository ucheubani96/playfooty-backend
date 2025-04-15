package com.playfooty.backendCore.exception;

public class UnauthorizedException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Invalid user credentials");
    }
}
