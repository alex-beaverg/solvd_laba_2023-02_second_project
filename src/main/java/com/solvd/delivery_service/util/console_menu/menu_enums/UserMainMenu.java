package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum UserMainMenu implements IMenu {
    CREATE_PACKAGE("Create package"),
    SHOW_CUSTOMER_PACKAGES("Show customer packages"),
    DELIVERY_COMPANY_MENU("<- DELIVERY COMPANY MENU"),
    EXIT("EXIT");

    private final String title;

    UserMainMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}