package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.GeneralActions;

import static com.solvd.delivery_service.util.Printers.*;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.MainMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.PackageMenu;
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.MenuItemOutOfBoundsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleMenu {
    private final static Logger LOGGER = LogManager.getLogger(ConsoleMenu.class);

    private int drawAnyMenuAndChooseMenuItem(String title, IMenu[] menuItems) {
        int index = 1;
        PRINT2LN.info(title);
        for (IMenu item : menuItems) {
            PRINTLN.info("[" + index + "] - " + item.getTitle());
            index++;
        }
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter the menu item number: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return answer;
    }

    private ConsoleMenu tearDown() {
        RequestMethods.closeScanner();
        PRINTLN.info("Good bye!");
        return null;
    }

    public ConsoleMenu runMainMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("MAIN MENU:", MainMenu.values());
        switch (answer) {
            case (1) -> {
                return runPackageMenu();
            }
            default -> {
                return tearDown();
            }
        }
    }

    public ConsoleMenu runPackageMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("PACKAGE MENU:", PackageMenu.values());
        switch (answer) {
            case (1) -> {
                GeneralActions.showPackages();
                return runPackageMenu();
            }
            case (2) -> {
                GeneralActions.createPackage();
                return runPackageMenu();
            }
            case (3) -> {
                return runMainMenu();
            }
            default -> {
                return tearDown();
            }
        }
    }
}
