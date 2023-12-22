package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminEmployeeMenu implements IMenu {
    SHOW_EMPLOYEES("Show all employees"),
    REGISTER_EMPLOYEE("Register new employee"),
    ADMIN_MAIN_MENU("Return to the ADMIN MAIN MENU"),
    EXIT("Exit");

    private final String title;

    AdminEmployeeMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
