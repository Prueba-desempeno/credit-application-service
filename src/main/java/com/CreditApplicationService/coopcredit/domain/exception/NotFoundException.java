package com.CreditApplicationService.coopcredit.domain.exception;

/**
 * Not found exception with Spanish message content.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
