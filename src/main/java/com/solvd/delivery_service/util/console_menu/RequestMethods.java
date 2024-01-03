package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.util.custom_exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.solvd.delivery_service.util.Printers.*;

import java.util.Scanner;

public class RequestMethods {
    protected static final Logger LOGGER = LogManager.getLogger(RequestMethods.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static int requestingInfoWithChoice(String text, int menuItemsNumber)
            throws EmptyInputException, NumberFormatException, MenuItemOutOfBoundsException {
        PRINT.info(text);
        String answer = scanner.nextLine();
        if (answer.isEmpty()) {
            throw new EmptyInputException("[EmptyInputException]: Entered data can not be empty!");
        }
        int numberFromAnswer = Integer.parseInt(answer);
        if (numberFromAnswer < 1 || numberFromAnswer > menuItemsNumber) {
            throw new MenuItemOutOfBoundsException("[MenuItemNumberOutOfBoundsException]: Entered data " +
                    "must be equal to some menu item!");
        }
        return numberFromAnswer;
    }

    public static String requestingInfoString(String text) throws EmptyInputException, StringFormatException {
        PRINT.info(text);
        String answer = scanner.nextLine();
        if (answer.isEmpty()) {
            throw new EmptyInputException("[EmptyInputException]: Entered data can not be empty!");
        }
        if (!answer.matches("^[a-zA-Zа-яёА-ЯЁ0-9. ]+$")) {
            throw new StringFormatException("[StringFormatException]: Entered data is not a correct string!");
        }
        return answer;
    }

    public static int requestingInfoInt(String text)
            throws EmptyInputException, NumberFormatException, NegativeNumberException {
        PRINT.info(text);
        String answer = scanner.nextLine();
        if (answer.isEmpty()) {
            throw new EmptyInputException("[EmptyInputException]: Entered data can not be empty!");
        }
        int numberFromAnswer = Integer.parseInt(answer);
        if (numberFromAnswer < 0) {
            throw new NegativeNumberException("[NegativeNumberException]: Entered data can not be negative");
        }
        return numberFromAnswer;
    }

    public static double requestingInfoDouble(String text)
            throws EmptyInputException, NumberFormatException, NegativeNumberException {
        PRINT.info(text);
        String answer = scanner.nextLine();
        if (answer.isEmpty()) {
            throw new EmptyInputException("[EmptyInputException]: Entered data can not be empty!");
        }
        double numberFromAnswer = Double.parseDouble(answer);
        if (numberFromAnswer < 0) {
            throw new NegativeNumberException("[NegativeNumberException]: Entered data can not be negative");
        }
        return numberFromAnswer;
    }

    public static Long getLongValueFromConsole(String name) {
        long value;
        do {
            try {
                value = requestingInfoInt("Enter " + name + ": ");
                break;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return value;
    }

    public static Integer getIntegerValueFromConsole(String name) {
        int value;
        do {
            try {
                value = requestingInfoInt("Enter " + name + ": ");
                break;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return value;
    }

    public static String getStringValueFromConsole(String name) {
        String value;
        do {
            try {
                value = requestingInfoString("Enter " + name + ": ");
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        return value;
    }

    public static int getNumberFromChoice(String name, int size) {
        int answer;
        do {
            try {
                answer = requestingInfoWithChoice("Enter " + name + ": ", size);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return answer;
    }

    static void closeScanner() {
        scanner.close();
    }
}