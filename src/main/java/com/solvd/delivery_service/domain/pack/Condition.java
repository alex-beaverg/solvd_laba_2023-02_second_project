package com.solvd.delivery_service.domain.pack;

public enum Condition {
    DAMAGED("Damaged"),
    NOT_DAMAGED("Not damaged"),
    UNKNOWN("Unknown");

    private final String title;

    Condition(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
