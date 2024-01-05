package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminEmployeeMenu implements IMenu {
    SHOW_EMPLOYEES("Show all employees"),
    REGISTER_EMPLOYEE("Register new employee"),
    REGISTER_EMPLOYEE_FROM_FILE("Register new employee FROM FILE"),
    UPDATE_EMPLOYEE_FIELD("Update existing employee field"),
    REMOVE_EMPLOYEE("Remove existing employee"),
    ADMIN_MAIN_MENU("<- ADMIN MAIN MENU"),
    EXIT("EXIT");

    private final String title;

    AdminEmployeeMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}