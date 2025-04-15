package com.playfooty.backendCore.exception;

public class InvalidJWTException extends UnauthorizedException{
    private static final long serialVersionUID = 1;

    public InvalidJWTException(String message) {
        super(message);
    }

    public InvalidJWTException() {
        super("Invalid token");
    }
}
