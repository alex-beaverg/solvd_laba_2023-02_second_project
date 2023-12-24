package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.service.PersonInfoService;
import com.solvd.delivery_service.service.impl.PersonInfoServiceDaoImpl;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;
import com.solvd.delivery_service.util.custom_exceptions.*;
import com.solvd.delivery_service.util.functional_interfaces.IPrintAsMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.solvd.delivery_service.util.Printers.*;

public class Actions {
    protected static final Logger LOGGER = LogManager.getLogger(Actions.class);
    protected static final IPrintAsMenu<Integer, String> printAsMenu = (index, line) -> PRINTLN.info("[" + index + "] - " + line);

    protected static Passport getPassportFromConsole() {
        return new Passport(RequestMethods.getStringValueFromConsole("passport number"));
    }

    protected static Address getAddressFromConsole() {
        Address address = new Address();
        address.setCountry((Country) getEnumFromConsole(Country.values(), "country"));
        address.setCity(RequestMethods.getStringValueFromConsole("city"));
        address.setStreet(RequestMethods.getStringValueFromConsole("street"));
        address.setHouse(RequestMethods.getIntegerValueFromConsole("house"));
        address.setFlat(RequestMethods.getIntegerValueFromConsole("flat"));
        address.setZipCode(RequestMethods.getIntegerValueFromConsole("zip code"));
        return address;
    }

    protected static PersonInfo getPersonInfoFromConsole(Passport passport, Address address) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setPassport(passport);
        personInfo.setAddress(address);
        personInfo.setFirstName(RequestMethods.getStringValueFromConsole("first name"));
        personInfo.setLastName(RequestMethods.getStringValueFromConsole("last name"));
        do {
            try {
                personInfo.setAge(getAgeFromConsole());
                break;
            } catch (AgeException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        return personInfo;
    }

    public static int getAgeFromConsole() throws AgeException {
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

    protected static PersonInfo getExistingPersonInfo() {
        PersonInfoService personInfoService = new PersonInfoServiceDaoImpl();
        List<PersonInfo> persons = personInfoService.retrieveAll();
        int index = 1;
        for (PersonInfo personInfo : persons) {
            printAsMenu.print(index, personInfo.getFirstName() + " " + personInfo.getLastName());
            index++;
        }
        return persons.get(RequestMethods.getNumberFromChoice("person number", index - 1) - 1);
    }

    protected static IMenu getEnumFromConsole(IMenu[] enumArray, String name) {
        int index = 1;
        PRINTLN.info("Choose the " + name + ":");
        for (IMenu item : enumArray) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        return enumArray[RequestMethods.getNumberFromChoice(name + " number", index - 1) - 1];
    }
}
