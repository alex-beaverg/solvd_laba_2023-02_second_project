package com.solvd.delivery_service.util.custom_exceptions;

public class EmptyInputException extends Exception {
    public EmptyInputException(String message) {
        super(message);
    }
}