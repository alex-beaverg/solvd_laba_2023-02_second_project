package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.UserActions;
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
                UserActions.showCustomerPackages();
                return runUserMainMenu();
            }
            case (3) -> {
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
                UserActions.createPackageWithExistingCustomer();
                return runUserMainMenu();
            }
            case (2) -> {
                UserActions.createPackageWithRegistrationNewCustomer();
                return runUserMainMenu();
            }
            case (3) -> {
                return runUserMainMenu();
            }
            default -> {
                return (ConsoleMenuUser) tearDown();
            }
        }
    }
}