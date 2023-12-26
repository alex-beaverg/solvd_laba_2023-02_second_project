package com.solvd.delivery_service.util.console_menu.menu_enums;

public enum UserGetCustomerMenu implements IMenu {
    FIND_EXIST_USER("Find exist customer by last name"),
    REGISTER_NEW_USER("Register new customer"),
    USER_MAIN_MENU("<- USER MAIN MENU"),
    EXIT("EXIT");

    private final String title;

    UserGetCustomerMenu(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}