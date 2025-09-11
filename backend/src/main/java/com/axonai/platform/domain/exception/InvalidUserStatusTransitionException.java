package com.axonai.platform.domain.exception;

public class InvalidUserStatusTransitionException extends UserDomainException {
    public InvalidUserStatusTransitionException(String message) {
        super(message);
    }
}
