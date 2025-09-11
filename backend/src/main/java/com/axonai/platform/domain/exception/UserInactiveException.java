package com.axonai.platform.domain.exception;

public class UserInactiveException extends UserDomainException {
    public UserInactiveException(String message) {
        super(message);
    }
}
