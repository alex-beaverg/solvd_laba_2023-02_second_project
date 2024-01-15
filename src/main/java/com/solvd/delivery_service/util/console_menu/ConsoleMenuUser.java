package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserGetCustomerMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserMainMenu;

public class ConsoleMenuUser extends ConsoleMenu {

    protected ConsoleMenuUser runUserMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("USER MAIN MENU:", UserMainMenu.values());
        switch (answer) {
            case (1) -> {
                return runPackageGetCustomerSubMenu();
            }
            case (2) -> {
                PARSER_ACTIONS_SERVICE.getParserActions().createPackageFromFile();
                return runUserMainMenu();
            }
            case (3) -> {
                Actions.showCustomerPackages();
                return runUserMainMenu();
            }
            case (4) -> {
                return (ConsoleMenuUser) runDeliveryCompanyMenu();
            }
            default -> {
                return (ConsoleMenuUser) tearDown();
            }
        }
    }

    public ConsoleMenuUser runPackageGetCustomerSubMenu() {
        ENTITY_ACTIONS_SERVICE.assignEntry("PACKAGE");
        int answer = drawAnyMenuAndChooseMenuItem("USER PACKAGE (GET CUSTOMER) SUB-MENU:", UserGetCustomerMenu.values());
        switch (answer) {
            case (1) -> {
                Actions.createPackageWithExistingCustomer();
                return runUserMainMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runUserMainMenu();
            }
            case (3) -> {
                PARSER_ACTIONS_SERVICE.getParserActions().createPackageWithRegistrationNewCustomerFromFile();
                return runUserMainMenu();
            }
            case (4) -> {
                return runUserMainMenu();
            }
            default -> {
                return (ConsoleMenuUser) tearDown();
            }
        }
    }
}