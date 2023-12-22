package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.AdminActions;
import com.solvd.delivery_service.util.console_menu.menu_enums.AdminMainMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.AdminPackageMenu;

import static com.solvd.delivery_service.util.Printers.*;

public class ConsoleMenuAdmin extends ConsoleMenu {

    protected ConsoleMenuAdmin runAdminMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN MAIN MENU:", AdminMainMenu.values());
        switch (answer) {
            case (1) -> {
                AdminActions.showNumberOfEntriesInDb();
                return runAdminMainMenu();
            }
            case (2) -> {
                PRINT2LN.info("RUN ADMIN EMPLOYEE MENU");
                return runAdminMainMenu();
            }
            case (3) -> {
                return runAdminPackageMenu();
            }
            case (4) -> {
                return (ConsoleMenuAdmin) runDeliveryServiceMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminPackageMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN PACKAGE MENU", AdminPackageMenu.values());
        switch (answer) {
            case (1) -> {
                AdminActions.showPackages();
                return runAdminPackageMenu();
            }
            case (2) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }
}
