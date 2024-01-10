package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.util.console_menu.menu_enums.DeliveryCompanyMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.DaoServiceMenu;
import com.solvd.delivery_service.util.console_menu.menu_enums.ParserServiceMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.solvd.delivery_service.util.Printers.*;

public class ConsoleMenu {
    protected static final Logger LOGGER = LogManager.getLogger(ConsoleMenu.class);
    protected static DaoService DAO_SERVICE = DaoService.getInstance();
    protected static ParserActionsService PARSER_ACTIONS_SERVICE = ParserActionsService.getInstance();

    public ConsoleMenu runServiceMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("DAO SERVICE MENU:", DaoServiceMenu.values());
        switch (answer) {
            case (1) -> {
                DAO_SERVICE.assignBasicDaoService();
                return runParserMenu();
            }
            case (2) -> {
                DAO_SERVICE.assignMybatisDaoService();
                return runParserMenu();
            }
            default -> {
                return tearDown();
            }
        }
    }

    protected ConsoleMenu runParserMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("PARSER SERVICE MENU:", ParserServiceMenu.values());
        switch (answer) {
            case (1) -> {
                PARSER_ACTIONS_SERVICE.assignStaxXmlParserActionsService();
                return runDeliveryCompanyMenu();
            }
            case (2) -> {
                PARSER_ACTIONS_SERVICE.assignJaxbXmlParserActionsService();
                return runDeliveryCompanyMenu();
            }
            case (3) -> {
                PARSER_ACTIONS_SERVICE.assignJacksonJsonParserActionsService();
                return runDeliveryCompanyMenu();
            }
            case (4) -> {
                return runServiceMenu();
            }
            default -> {
                return tearDown();
            }
        }
    }

    protected ConsoleMenu runDeliveryCompanyMenu() {
        int answer = drawAnyMenuAndChooseMenuItem("DELIVERY COMPANY MENU:", DeliveryCompanyMenu.values());
        switch (answer) {
            case (1) -> {
                return new ConsoleMenuUser().runUserMainMenu();
            }
            case (2) -> {
                return authentication();
            }
            case (3) -> {
                return runParserMenu();
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
            PRINTLN.info("[" + index + "]: " + item.getTitle());
            index++;
        }
        return RequestMethods.getNumberFromChoice("the menu item number", index - 1);
    }

    protected ConsoleMenu tearDown() {
        RequestMethods.closeScanner();
        PRINTLN.info("GOOD BYE!");
        return null;
    }

    private ConsoleMenu authentication() {
        PRINT2LN.info("AUTHENTICATION");
        String password = RequestMethods.getStringValueFromConsole("admin password");
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
        LOGGER.error("[Warning]: Password is incorrect!");
        return runDeliveryCompanyMenu();
    }
}