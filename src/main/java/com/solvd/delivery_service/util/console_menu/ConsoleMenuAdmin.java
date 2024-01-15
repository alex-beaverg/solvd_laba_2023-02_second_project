package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.util.console_menu.menu_enums.*;

public class ConsoleMenuAdmin extends ConsoleMenu {

    protected ConsoleMenuAdmin runAdminMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN MAIN MENU:", AdminMainMenu.values());
        switch (answer) {
            case (1) -> {
                Actions.showNumberOfDatabaseEntries();
                return runAdminMainMenu();
            }
            case (2) -> {
                return runAdminCompanyMenu();
            }
            case (3) -> {
                return runAdminDepartmentMenu();
            }
            case (4) -> {
                return runAdminEmployeeMenu();
            }
            case (5) -> {
                return runAdminCustomerMenu();
            }
            case (6) -> {
                return runAdminPackageMenu();
            }
            case (7) -> {
                return (ConsoleMenuAdmin) runDeliveryCompanyMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminPackageMenu() {
        ENTITY_ACTIONS_SERVICE.assignEntry("PACKAGE");
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN PACKAGE MENU", AdminPackageMenu.values());
        switch (answer) {
            case (1) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().showEntityEntries();
                return runAdminPackageMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runAdminPackageMenu();
            }
            case (3) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().updateEntityEntryField();
                return runAdminPackageMenu();
            }
            case (4) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().removeEntityEntry();
                return runAdminPackageMenu();
            }
            case (5) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminEmployeeMenu() {
        ENTITY_ACTIONS_SERVICE.assignEntry("EMPLOYEE");
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN EMPLOYEE MENU", AdminEmployeeMenu.values());
        switch (answer) {
            case (1) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().showEntityEntries();
                return runAdminEmployeeMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runAdminEmployeeMenu();
            }
            case (3) -> {
                PARSER_ACTIONS_SERVICE.getParserActions().registerEmployeeFromFile();
                return runAdminEmployeeMenu();
            }
            case (4) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().updateEntityEntryField();
                return runAdminEmployeeMenu();
            }
            case (5) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().removeEntityEntry();
                return runAdminEmployeeMenu();
            }
            case (6) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminDepartmentMenu() {
        ENTITY_ACTIONS_SERVICE.assignEntry("DEPARTMENT");
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN DEPARTMENT MENU", AdminDepartmentMenu.values());
        switch (answer) {
            case (1) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().showEntityEntries();
                return runAdminDepartmentMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runAdminDepartmentMenu();
            }
            case (3) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().updateEntityEntryField();
                return runAdminDepartmentMenu();
            }
            case (4) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().removeEntityEntry();
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

    private ConsoleMenuAdmin runAdminCompanyMenu() {
        ENTITY_ACTIONS_SERVICE.assignEntry("COMPANY");
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN COMPANY MENU", AdminCompanyMenu.values());
        switch (answer) {
            case (1) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().showEntityEntries();
                return runAdminCompanyMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runAdminCompanyMenu();
            }
            case (3) -> {
                PARSER_ACTIONS_SERVICE.getParserActions().registerCompanyFromFile();
                return runAdminCompanyMenu();
            }
            case (4) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().updateEntityEntryField();
                return runAdminCompanyMenu();
            }
            case (5) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().removeEntityEntry();
                return runAdminCompanyMenu();
            }
            case (6) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }

    private ConsoleMenuAdmin runAdminCustomerMenu() {
        ENTITY_ACTIONS_SERVICE.assignEntry("CUSTOMER");
        int answer = drawAnyMenuAndChooseMenuItem("ADMIN CUSTOMER MENU", AdminCustomerMenu.values());
        switch (answer) {
            case (1) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().showEntityEntries();
                return runAdminCustomerMenu();
            }
            case (2) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().registerEntityEntry();
                return runAdminCustomerMenu();
            }
            case (3) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().updateEntityEntryField();
                return runAdminCustomerMenu();
            }
            case (4) -> {
                ENTITY_ACTIONS_SERVICE.getEntityActions().removeEntityEntry();
                return runAdminCustomerMenu();
            }
            case (5) -> {
                return runAdminMainMenu();
            }
            default -> {
                return (ConsoleMenuAdmin) tearDown();
            }
        }
    }
}