package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminCustomerMenu implements IMenu {
    SHOW_CUSTOMERS("Show all customers"),
    REGISTER_CUSTOMER("Register new customer"),
    UPDATE_CUSTOMER_FIELD("Update existing customer field"),
    REMOVE_CUSTOMER("Remove existing customer"),
    ADMIN_MAIN_MENU("<-- ADMIN MAIN MENU"),
    EXIT("EXIT");

    private final String title;

    AdminCustomerMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}