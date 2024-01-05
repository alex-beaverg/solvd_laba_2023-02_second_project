package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminCompanyMenu implements IMenu {
    SHOW_COMPANIES("Show all companies"),
    REGISTER_COMPANY("Register new company"),
    REGISTER_COMPANY_FROM_FILE("Register new company FROM FILE"),
    RENAME_COMPANY("Rename existing company"),
    REMOVE_COMPANY("Remove existing company"),
    ADMIN_MAIN_MENU("<- ADMIN MAIN MENU"),
    EXIT("EXIT");

    private final String title;

    AdminCompanyMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
