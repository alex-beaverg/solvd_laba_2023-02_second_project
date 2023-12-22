package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminMainMenu implements IMenu {
    SHOW_NUMBER_OF_ENTRIES_IN_DB("Show number of entries in database"),
    ADMIN_EMPLOYEE_MENU("Go to the ADMIN EMPLOYEE MENU"),
    ADMIN_PACKAGE_MENU("Go to the ADMIN PACKAGE MENU"),
    DELIVERY_SERVICE_MENU("Return to the DELIVERY SERVICE MENU"),
    EXIT("Exit");

    private final String title;

    AdminMainMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
