package com.axonai.platform.domain.exception;

public class UserNotVerifiedException extends UserDomainException {
    public UserNotVerifiedException(String message) {
        super(message);
    }
}