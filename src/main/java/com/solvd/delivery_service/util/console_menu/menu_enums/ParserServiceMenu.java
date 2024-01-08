package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum ParserServiceMenu implements IMenu {
    USE_STAX_XML_PARSER("Use STAX XML PARSER service"),
    USE_JAXB_XML_PARSER("Use JAXB XML PARSER service"),
    USE_JACKSON_JSON_PARSER("Use JACKSON JSON PARSER service"),
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
