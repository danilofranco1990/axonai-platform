package com.axonai.platform.identity.domain.exception;

public class InvalidUserStatusTransitionException extends UserDomainException {
    public InvalidUserStatusTransitionException(String message) {
        super(message);
    }
}
