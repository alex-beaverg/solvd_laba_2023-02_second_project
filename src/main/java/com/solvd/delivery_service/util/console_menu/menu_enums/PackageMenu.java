package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum PackageMenu implements IMenu {
    SHOW_PACKAGES("Show all packages"),
    CREATE_PACKAGE("Create package"),
    RETURN_TO_THE_MAIN_MENU("Return to the Main menu"),
    EXIT("Exit");

    private final String title;

    PackageMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
