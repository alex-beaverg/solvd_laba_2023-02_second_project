package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.entity_actions.CustomerActions;
import com.solvd.delivery_service.domain.actions.entity_actions.PackageActions;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserGetCustomerMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserMainMenu;

public class ConsoleMenuUser extends ConsoleMenu {

    protected ConsoleMenuUser runUserMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("USER MAIN MENU:", UserMainMenu.values());
        switch (answer) {
            case (1) -> {
                ENTITY_ACTIONS_SERVICE.assignEntry("PACKAGE");
                return runPackageGetCustomerSubMenu();
            }
            case (2) -> {
                PARSER_ACTIONS_SERVICE.getParserActions().createPackageFromFile();
                return runUserMainMenu();
            }
            case (3) -> {
                CustomerActions.showCustomerPackages();
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
        int answer = drawAnyMenuAndChooseMenuItem("USER PACKAGE (GET CUSTOMER) SUB-MENU:", UserGetCustomerMenu.values());
        switch (answer) {
            case (1) -> {
                PackageActions.createPackageWithExistingCustomer();
                return runPackageGetCustomerSubMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runPackageGetCustomerSubMenu();
            }
            case (3) -> {
                PARSER_ACTIONS_SERVICE.getParserActions().createPackageWithRegistrationNewCustomerFromFile();
                return runPackageGetCustomerSubMenu();
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