package com.solvd.delivery_service.domain.actions.parser_actions;

public enum Parser {
    STAX("STAX", 1, "STAX (XML) PARSER"),
    JAXB("JAXB", 2, "JAXB (XML) PARSER"),
    JACKSON("JACKSON", 3, "JACKSON (JSON) PROCESSOR");

    private final String title;
    private final int number;
    private final String description;

    Parser(String title, int number, String description) {
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
