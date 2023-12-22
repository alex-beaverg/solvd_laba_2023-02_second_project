package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminDepartmentMenu implements IMenu {
    SHOW_DEPARTMENTS("Show all departments"),
    REGISTER_DEPARTMENT("Register new department"),
    ADMIN_MAIN_MENU("Return to the ADMIN MAIN MENU"),
    EXIT("Exit");

    private final String title;

    AdminDepartmentMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
