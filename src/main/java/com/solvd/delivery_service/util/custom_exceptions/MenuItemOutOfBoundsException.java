package com.solvd.delivery_service.util.custom_exceptions;

public class MenuItemOutOfBoundsException extends Exception {
    public MenuItemOutOfBoundsException(String message) {
        super(message);
    }
}