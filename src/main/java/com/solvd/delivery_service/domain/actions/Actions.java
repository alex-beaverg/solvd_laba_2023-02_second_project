package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.*;
import com.solvd.delivery_service.util.functional_interfaces.IPrintAsMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.solvd.delivery_service.util.Printers.PRINTLN;

public class Actions {
    protected static final Logger LOGGER = LogManager.getLogger(Actions.class);
    protected static final IPrintAsMenu<Integer, String> printAsMenu = (index, line) -> PRINTLN.info("[" + index + "] - " + line);

    protected static Passport setPassport() {
        String passportNumber;
        do {
            try {
                passportNumber = RequestMethods.requestingInfoString("Enter your passport number: ");
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        return new Passport(passportNumber);
    }

    protected static Address setAddress() {
        Address address = new Address();
        int index = 1;
        PRINTLN.info("Choose the country:");
        for (Country item : Country.values()) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter number of country: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        address.setCountry(Country.values()[answer - 1]);
        do {
            try {
                address.setCity(RequestMethods.requestingInfoString("Enter city: "));
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        do {
            try {
                address.setStreet(RequestMethods.requestingInfoString("Enter street: "));
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        do {
            try {
                address.setHouse(RequestMethods.requestingInfoInt("Enter house number: "));
                break;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        do {
            try {
                address.setFlat(RequestMethods.requestingInfoInt("Enter flat number: "));
                break;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        do {
            try {
                address.setZipCode(RequestMethods.requestingInfoInt("Enter zip code: "));
                break;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return address;
    }

    protected static PersonInfo setPersonInfo(Passport passport, Address address) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setPassport(passport);
        personInfo.setAddress(address);
        do {
            try {
                personInfo.setFirstName(RequestMethods.requestingInfoString("Enter first name: "));
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        do {
            try {
                personInfo.setLastName(RequestMethods.requestingInfoString("Enter last name: "));
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        do {
            try {
                personInfo.setAge(setAge());
                break;
            } catch (AgeException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        return personInfo;
    }

    protected static int setAge() throws AgeException {
        do {
            try {
                int age = RequestMethods.requestingInfoInt("Enter age: ");
                if (age > 122) {
                    throw new AgeException("[AgeException]: Age can not be more than 122 years");
                }
                return age;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
    }
}
