package com.solvd.delivery_service.domain.pack;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;

public enum Condition implements IMenu {
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

    public String getName() {
        return this.name();
    }
}
