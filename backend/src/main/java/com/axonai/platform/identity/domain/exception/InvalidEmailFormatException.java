package com.axonai.platform.identity.domain.exception;

public class InvalidEmailFormatException extends UserDomainException {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
