package com.axonai.platform.identity.application.exception;

public class ConflictException extends AxonAlException {
    public ConflictException(String message) {
        super(message);
    }
}
