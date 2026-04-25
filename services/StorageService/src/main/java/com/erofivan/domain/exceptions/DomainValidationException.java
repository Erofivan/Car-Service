package com.erofivan.domain.exceptions;

public final class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
