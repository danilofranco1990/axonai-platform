package com.axonai.platform.domain.exception;

public class UserDomainException extends RuntimeException {
    public UserDomainException(String message) {
        super(message);
    }
}
