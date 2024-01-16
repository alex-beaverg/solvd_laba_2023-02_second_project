package com.solvd.delivery_service.domain.actions.console_actions;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;
import com.solvd.delivery_service.util.custom_exceptions.AgeException;
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.NegativeNumberException;

import static com.solvd.delivery_service.util.Printers.*;

public class DataInfoActions extends Actions {

    public static PersonInfo getPersonInfoFromConsole(Passport passport, Address address) {
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

    public static Address getAddressFromConsole() {
        Address address = new Address();
        address.setCountry((Country) chooseEnumValueFromConsole(Country.values(), "country"));
        address.setCity(RequestMethods.getStringValueFromConsole("city"));
        address.setStreet(RequestMethods.getStringValueFromConsole("street"));
        address.setHouse(RequestMethods.getIntegerValueFromConsole("house"));
        address.setFlat(RequestMethods.getIntegerValueFromConsole("flat"));
        address.setZipCode(RequestMethods.getIntegerValueFromConsole("zip code"));
        return address;
    }

    public static IMenu chooseEnumValueFromConsole(IMenu[] enumArray, String name) {
        int index = 1;
        PRINTLN.info("Choose the " + name + ":");
        for (IMenu item : enumArray) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        return enumArray[RequestMethods.getNumberFromChoice(name + " number", index - 1) - 1];
    }

    private static int getAgeFromConsole() throws AgeException {
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