package com.solvd.delivery_service.domain.pack;

public enum Status {
    DELIVERED("Delivered"),
    LOST("Lost");

    private final String title;

    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
