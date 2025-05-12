package com.runaumov.spring.exception;

public class RegistrationFailedException extends RuntimeException {

    public RegistrationFailedException(String message) {
        super(message);
    }
}
