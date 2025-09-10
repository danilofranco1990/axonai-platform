package com.axonai.platform.domain.exception;

public class InvalidEmailFormatException extends UserDomainException {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
