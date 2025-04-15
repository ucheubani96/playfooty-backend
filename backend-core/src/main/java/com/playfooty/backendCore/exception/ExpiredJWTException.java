package com.playfooty.backendCore.exception;

public class ExpiredJWTException extends UnauthorizedException{
    private static final long serialVersionUID = 1;

    public ExpiredJWTException(String message) {
        super(message);
    }

    public ExpiredJWTException() {
        super("Token expired");
    }
}
