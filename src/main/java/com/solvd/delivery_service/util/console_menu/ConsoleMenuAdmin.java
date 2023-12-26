package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.AdminActions;
import com.solvd.delivery_service.util.console_menu.menu_enums.*;

public class ConsoleMenuAdmin extends ConsoleMenu {

    protected ConsoleMenuAdmin runAdminMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN MAIN MENU:", AdminMainMenu.values());
        switch (answer) {
            case (1) -> {
                AdminActions.showNumberOfDatabaseEntries();
                return runAdminMainMenu();
            }
            case (2) -> {
                return runAdminDepartmentMenu();
            }
            case (3) -> {
                return runAdminEmployeeMenu();
            }
            case (4) -> {
                return runAdminCustomerMenu();
            }
            case (5) -> {
                return runAdminPackageMenu();
            }
            case (6) -> {
                return (ConsoleMenuAdmin) runDeliveryCompanyMenu();
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
                AdminActions.updatePackageField();
                return runAdminPackageMenu();
            }
            case (3) -> {
                AdminActions.removePackage();
                return runAdminPackageMenu();
            }
            case (4) -> {
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
                AdminActions.updateEmployeeField();
                return runAdminEmployeeMenu();
            }
            case (4) -> {
                AdminActions.removeEmployee();
                return runAdminEmployeeMenu();
            }
            case (5) -> {
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
                AdminActions.renameDepartment();
                return runAdminDepartmentMenu();
            }
            case (4) -> {
                AdminActions.removeDepartment();
                return runAdminDepartmentMenu();
            }
            case (5) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminCustomerMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN CUSTOMER MENU", AdminCustomerMenu.values());
        switch (answer) {
            case (1) -> {
                AdminActions.showCustomers();
                return runAdminCustomerMenu();
            }
            case (2) -> {
                AdminActions.updateCustomerField();
                return runAdminCustomerMenu();
            }
            case (3) -> {
                AdminActions.removeCustomer();
                return runAdminCustomerMenu();
            }
            case (4) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }
}