package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminPackageMenu implements IMenu {
    SHOW_PACKAGES("Show all packages"),
    ADMIN_MAIN_MENU("Return to the ADMIN MAIN MENU"),
    EXIT("Exit");

    private final String title;

    AdminPackageMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
