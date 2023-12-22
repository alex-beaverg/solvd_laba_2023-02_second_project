package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.util.console_menu.menu_enums.DeliveryServiceMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.MenuItemOutOfBoundsException;
import com.solvd.delivery_service.util.custom_exceptions.StringFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.solvd.delivery_service.util.Printers.*;

public class ConsoleMenu {
    protected final static Logger LOGGER = LogManager.getLogger(ConsoleMenu.class);

    public ConsoleMenu runDeliveryServiceMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("DELIVERY SERVICE MENU:", DeliveryServiceMenu.values());
        switch (answer) {
            case (1) -> {
                return new ConsoleMenuUser().runUserMainMenu();
            }
            case (2) -> {
                String password;
                PRINT2LN.info("AUTHENTICATION");
                do {
                    try {
                        password = RequestMethods.requestingInfoString("Enter admin password: ");
                        break;
                    } catch (EmptyInputException | StringFormatException e) {
                        LOGGER.error(e.getMessage());
                    }
                } while (true);
                Properties property = new Properties();
                try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
                    property.load(fis);
                    if (password.equals(property.getProperty("admin.password"))) {
                        PRINTLN.info("[Info]: Password is correct!");
                        return new ConsoleMenuAdmin().runAdminMainMenu();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("You have been problem with reading from property file!", e);
                }
                PRINTLN.info("[Info]: Password is incorrect!");
                return runDeliveryServiceMenu();
            }
            default -> {
                return tearDown();
            }
        }
    }

    protected int drawAnyMenuAndChooseMenuItem(String title, IMenu[] menuItems) {
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

    protected ConsoleMenu tearDown() {
        RequestMethods.closeScanner();
        PRINTLN.info("GOOD BYE!");
        return null;
    }
}
