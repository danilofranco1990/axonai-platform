package com.axonai.platform.identity.domain.exception;

public class UserNotVerifiedException extends UserDomainException {
    public UserNotVerifiedException(String message) {
        super(message);
    }
}
