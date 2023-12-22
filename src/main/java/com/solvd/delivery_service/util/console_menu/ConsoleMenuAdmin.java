package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.AdminActions;
import com.solvd.delivery_service.util.console_menu.menu_enums.AdminDepartmentMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.AdminEmployeeMenu;
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
                return runAdminDepartmentMenu();
            }
            case (3) -> {
                return runAdminEmployeeMenu();
            }
            case (4) -> {
                return runAdminPackageMenu();
            }
            case (5) -> {
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

    private ConsoleMenuAdmin runAdminEmployeeMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN EMPLOYEE MENU", AdminEmployeeMenu.values());
        switch (answer) {
            case (1) -> {
                AdminActions.showEmployees();
                return runAdminEmployeeMenu();
            }
            case (2) -> {
                AdminActions.registerEmployee();
                return runAdminEmployeeMenu();
            }
            case (3) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminDepartmentMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN DEPARTMENT MENU", AdminDepartmentMenu.values());
        switch (answer) {
            case (1) -> {
                AdminActions.showDepartments();
                return runAdminDepartmentMenu();
            }
            case (2) -> {
                AdminActions.registerDepartment();
                return runAdminDepartmentMenu();
            }
            case (3) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }
}
