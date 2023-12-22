package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum DeliveryServiceMenu implements IMenu {
    USER_MAIN_MENU("Use delivery service as a user"),
    ADMIN_MAIN_MENU("Use delivery service as an admin"),
    EXIT("Exit");

    private final String title;

    DeliveryServiceMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
