package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum DaoServiceMenu implements IMenu {
    USE_BASIC_DAO("Use BASIC DAO service"),
    USE_MYBATIS_DAO("Use MYBATIS DAO service"),
    EXIT("EXIT");

    private final String title;

    DaoServiceMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}