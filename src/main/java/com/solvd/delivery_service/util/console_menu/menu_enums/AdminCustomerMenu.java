package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum AdminCustomerMenu implements IMenu {
    SHOW_CUSTOMERS("Show all customers"),
    UPDATE_CUSTOMER_FIELD("Update existing customer field"),
    REMOVE_CUSTOMER("Remove existing customer"),
    ADMIN_MAIN_MENU("Return to the ADMIN MAIN MENU"),
    EXIT("Exit");

    private final String title;

    AdminCustomerMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
