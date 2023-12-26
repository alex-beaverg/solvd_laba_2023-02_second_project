package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum ServiceMenu implements IMenu {
    USING_DAO("Use DAO service"),
    USING_MYBATIS("Use MYBATIS service"),
    EXIT("EXIT");

    private final String title;

    ServiceMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}