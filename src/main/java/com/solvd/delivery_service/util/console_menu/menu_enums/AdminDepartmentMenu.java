package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminDepartmentMenu implements IMenu {
    SHOW_DEPARTMENTS("Show all departments"),
    REGISTER_DEPARTMENT("Register new department"),
    UPDATE_DEPARTMENT("Update existing department field"),
    REMOVE_DEPARTMENT("Remove existing department"),
    ADMIN_MAIN_MENU("<-- ADMIN MAIN MENU"),
    EXIT("EXIT");

    private final String title;

    AdminDepartmentMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}