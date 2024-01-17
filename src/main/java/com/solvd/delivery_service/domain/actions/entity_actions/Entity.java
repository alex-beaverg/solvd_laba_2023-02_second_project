package com.solvd.delivery_service.domain.actions.entity_actions;

public enum Entity {
    COMPANY("COMPANY", 1, "COMPANY ACTIONS"),
    DEPARTMENT("DEPARTMENT", 2, "DEPARTMENT ACTIONS"),
    EMPLOYEE("EMPLOYEE", 3, "EMPLOYEE ACTIONS"),
    CUSTOMER("CUSTOMER", 4, "CUSTOMER ACTIONS"),
    PACKAGE("PACKAGE", 5, "PACKAGE ACTIONS");

    private final String title;
    private final int number;
    private final String description;

    Entity(String title, int number, String description) {
        this.title = title;
        this.number = number;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
}