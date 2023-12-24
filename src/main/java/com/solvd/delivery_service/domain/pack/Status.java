package com.solvd.delivery_service.domain.pack;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;

public enum Status implements IMenu {
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
