package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminPackageMenu implements IMenu {
    SHOW_PACKAGES("Show all packages"),
    UPDATE_PACKAGE_FIELD("Update existing package field"),
    REMOVE_PACKAGE("Remove existing package"),
    ADMIN_MAIN_MENU("<-- ADMIN MAIN MENU"),
    EXIT("EXIT");

    private final String title;

    AdminPackageMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}