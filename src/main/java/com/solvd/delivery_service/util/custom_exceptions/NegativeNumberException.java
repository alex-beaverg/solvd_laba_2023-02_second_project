package com.solvd.delivery_service.util.custom_exceptions;

public class NegativeNumberException extends Exception {
    public NegativeNumberException(String message) {
        super(message);
    }
}