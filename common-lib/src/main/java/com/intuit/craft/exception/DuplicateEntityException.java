package com.intuit.craft.exception;

public class DuplicateEntityException extends MovieBookingException{

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable e) {
        super(message, e);
    }
}
