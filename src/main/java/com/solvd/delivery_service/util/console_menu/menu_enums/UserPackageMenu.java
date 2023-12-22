package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum UserPackageMenu implements IMenu {
    CREATE_PACKAGE("Create package"),
    USER_MAIN_MENU("Return to the USER MAIN MENU"),
    EXIT("Exit");

    private final String title;

    UserPackageMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
