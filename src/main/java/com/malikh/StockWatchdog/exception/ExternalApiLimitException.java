package com.malikh.stockwatchdog.exception;

public class ExternalApiLimitException extends RuntimeException {
    public ExternalApiLimitException(String message) {
        super(message);
    }
}
