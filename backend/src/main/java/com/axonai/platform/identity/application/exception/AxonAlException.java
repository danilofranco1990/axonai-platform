package com.axonai.platform.identity.application.exception;

public abstract class AxonAlException extends RuntimeException {
    public AxonAlException(String message) {
        super(message);
    }
}
