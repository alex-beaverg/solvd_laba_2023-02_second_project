package com.solvd.delivery_service.domain.actions.entity;

public enum Entity {
    COMPANY("COMPANY", 1),
    DEPARTMENT("DEPARTMENT", 2),
    EMPLOYEE("EMPLOYEE", 3),
    CUSTOMER("CUSTOMER", 4),
    PACKAGE("PACKAGE", 5);

    private final String title;
    private final int number;

    Entity(String title, int number) {
        this.title = title;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }
}