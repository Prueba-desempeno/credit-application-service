package com.CreditApplicationService.coopcredit.domain.exception;


/**
 * Generic domain exception. Messages intended for clients are in Spanish as requested.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
