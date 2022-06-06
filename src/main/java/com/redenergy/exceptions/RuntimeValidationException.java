package com.redenergy.exceptions;

public class RuntimeValidationException extends RuntimeException {
    public RuntimeValidationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
