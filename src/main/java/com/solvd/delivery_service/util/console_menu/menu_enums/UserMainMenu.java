package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum UserMainMenu implements IMenu {
    USER_PACKAGE_MENU("Go to the USER PACKAGE MENU"),
    DELIVERY_SERVICE_MENU("Return to the DELIVERY SERVICE MENU"),
    EXIT("Exit");

    private final String title;

    UserMainMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
