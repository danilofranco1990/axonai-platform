package com.axonai.platform.identity.domain.exception;

public class UserInactiveException extends UserDomainException {
    public UserInactiveException(String message) {
        super(message);
    }
}
