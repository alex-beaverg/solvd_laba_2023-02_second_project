package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.UserActions;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserGetCustomerMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserMainMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.UserPackageMenu;

import static com.solvd.delivery_service.util.Printers.*;

public class ConsoleMenuUser extends ConsoleMenu {

    protected ConsoleMenuUser runUserMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("USER MAIN MENU:", UserMainMenu.values());
        switch (answer) {
            case (1) -> {
                return runUserPackageMenu();
            }
            case (2) -> {
                return (ConsoleMenuUser) runDeliveryServiceMenu();
            }
            default -> {
                return (ConsoleMenuUser) tearDown();
            }
        }
    }

    public ConsoleMenuUser runUserPackageMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("USER PACKAGE MENU:", UserPackageMenu.values());
        switch (answer) {
            case (1) -> {
                return runGetCustomerMenu();
            }
            case (2) -> {
                return runUserMainMenu();
            }
            default -> {
                return (ConsoleMenuUser) tearDown();
            }
        }
    }

    public ConsoleMenuUser runGetCustomerMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("USER GET CUSTOMER MENU:", UserGetCustomerMenu.values());
        switch (answer) {
            case (1) -> {
                UserActions.createPackageWithExistCustomer();
                return runUserPackageMenu();
            }
            case (2) -> {
                UserActions.createPackageWithCreatingCustomer();
                return runUserPackageMenu();
            }
            case (3) -> {
                return runUserPackageMenu();
            }
            default -> {
                return (ConsoleMenuUser) tearDown();
            }
        }
    }
}
