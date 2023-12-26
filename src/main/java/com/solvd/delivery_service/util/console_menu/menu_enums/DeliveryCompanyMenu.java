package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum DeliveryCompanyMenu implements IMenu {
    USER_MAIN_MENU("USER MENU"),
    ADMIN_MAIN_MENU("ADMIN MENU"),
    SERVICE_MENU("<- SERVICE MENU"),
    EXIT("EXIT");

    private final String title;

    DeliveryCompanyMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}