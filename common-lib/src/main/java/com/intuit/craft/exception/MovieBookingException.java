package com.intuit.craft.exception;

public class MovieBookingException extends RuntimeException {

    public MovieBookingException(String message) {
        super(message);
    }

    public MovieBookingException(String message, Throwable e) {
        super(message, e);
    }
}
