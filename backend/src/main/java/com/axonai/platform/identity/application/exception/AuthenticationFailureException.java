package com.axonai.platform.identity.application.exception;

public class AuthenticationFailureException extends AxonAlException {
    public AuthenticationFailureException(String message) {
        super(message);
    }
}