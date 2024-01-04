package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum ParserServiceMenu implements IMenu {
    USE_STAX_PARSER("Use STAX PARSER service"),
    USE_JAXB_PARSER("Use JAXB PARSER service"),
    DAO_SERVICE_MENU("<- DAO SERVICE MENU"),
    EXIT("EXIT");

    private final String title;

    ParserServiceMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
