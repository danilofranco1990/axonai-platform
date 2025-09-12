package com.axonai.platform.identity.domain.exception;

public class UserDomainException extends RuntimeException {
    public UserDomainException(String message) {
        super(message);
    }
}
