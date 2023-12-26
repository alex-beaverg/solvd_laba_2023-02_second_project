package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminMainMenu implements IMenu {
    SHOW_NUMBER_OF_ENTRIES_IN_DB("Show number of entries in database"),
    ADMIN_DEPARTMENT_MENU("ADMIN DEPARTMENT MENU"),
    ADMIN_EMPLOYEE_MENU("ADMIN EMPLOYEE MENU"),
    ADMIN_CUSTOMER_MENU("ADMIN CUSTOMER MENU"),
    ADMIN_PACKAGE_MENU("ADMIN PACKAGE MENU"),
    DELIVERY_COMPANY_MENU("<- DELIVERY COMPANY MENU"),
    EXIT("EXIT");

    private final String title;

    AdminMainMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}