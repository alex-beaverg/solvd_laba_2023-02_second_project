package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum MainMenu implements IMenu {
    PACKAGE_MENU("Go to the Package menu"),
    EXIT("Exit");

    private final String title;

    MainMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
