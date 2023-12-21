package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PackageService;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.*;
import com.solvd.delivery_service.util.functional_interfaces.IPrintAsMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.solvd.delivery_service.util.Printers.*;

public class GeneralActions {
    private static final Logger LOGGER = LogManager.getLogger(GeneralActions.class);
    private static final IPrintAsMenu<Integer, String> printAsMenu = (index, line) -> PRINTLN.info("[" + index + "] - " + line);

    public static void createPackage() {
        PRINT2LN.info("REGISTRATION OF A CUSTOMER");
        Passport passport = registerPassport();
        Address customerAddress = registerAddress();
        PersonInfo customerPersonInfo = registerPersonInfo(passport, customerAddress);
        Customer customer = registerCustomer(customerPersonInfo);
        Employee employee = setRandomEmployee();
        PRINT2LN.info("REGISTRATION OF A PACKAGE");
        registerPackage(customer, employee);
    }

    private static Passport registerPassport() {
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

    private static Address registerAddress() {
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

    private static PersonInfo registerPersonInfo(Passport passport, Address address) {
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
                personInfo.setAge(getAgeFromConsole());
                break;
            } catch (AgeException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        return personInfo;
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

    private static Customer registerCustomer(PersonInfo personInfo) {
        return new Customer(personInfo);
    }

    private static Employee setRandomEmployee() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        Random random = new Random();
        return employeeService.retrieveAll().get(random.nextInt(employeeService.retrieveAll().size()));
    }

    private static void registerPackage(Customer customer, Employee employee) {
        PackageService packageService = new PackageServiceImpl();
        Package pack = new Package();
        pack.setCustomer(customer);
        pack.setEmployee(employee);
        pack.setNumber(packageService.retrieveMaxPackageNumber() + 1);

        double weight;
        do {
            try {
                weight = getWeightFromConsole();
                break;
            } catch (WeightException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        pack.setPackageType(setPackageTypeFromWeight(weight));

        int index = 1;
        PRINTLN.info("Choose the delivery type:");
        for (DeliveryType item : DeliveryType.values()) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter number of delivery type: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        pack.setDeliveryType(DeliveryType.values()[answer - 1]);
        pack.setAddressFrom(pack.getCustomer().getPersonInfo().getAddress());
        PRINT2LN.info("ADDRESS 'TO' REGISTRATION");
        Address addressTo = registerAddress();
        pack.setAddressTo(addressTo);
        int zones = Accounting.calculateZones(pack.getCustomer().getPersonInfo().getAddress(), addressTo);
        pack.setStatus(setRandomStatus(pack.getDeliveryType(), zones));
        pack.setCondition(setRandomCondition(pack.getStatus(), pack.getDeliveryType(), zones));
        packageService.create(pack);
        PRINT2LN.info("PACKAGE WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
    }

    private static double getWeightFromConsole() throws WeightException {
        do {
            try {
                double weight = RequestMethods.requestingInfoDouble("Enter package weight in kg (0...100): ");
                if (weight > 100.0) {
                    throw new WeightException("[WeightException]: Weight can not be more than 100 kg!");
                }
                return weight;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
    }

    private static PackageType setPackageTypeFromWeight(double weight) {
        for (PackageType p_type : PackageType.values()) {
            if (weight <= p_type.getMaxWeight()) return p_type;
        }
        return null;
    }

    private static Status setRandomStatus(DeliveryType deliveryType, int zones) {
        Status status;
        Random random = new Random();
        switch (deliveryType.name()) {
            case ("REGULAR") -> status =
                    random.nextInt(zones * 2) == random.nextInt(zones * 2) ?
                            Status.LOST : Status.DELIVERED;
            case ("EXPRESS") -> status =
                    random.nextInt(zones * 10) == random.nextInt(zones * 10) ?
                            Status.LOST : Status.DELIVERED;
            default -> status = Status.DELIVERED;
        }
        return status;
    }

    private static Condition setRandomCondition(Status status, DeliveryType deliveryType, int zones) {
        Condition condition;
        Random random = new Random();
        if (status == Status.LOST) {
            condition = Condition.UNKNOWN;
        } else {
            switch (deliveryType.name()) {
                case ("REGULAR") -> condition =
                        random.nextInt(zones * 2) == random.nextInt(zones * 2) ?
                                Condition.DAMAGED : Condition.NOT_DAMAGED;
                case ("EXPRESS") -> condition =
                        random.nextInt(zones * 10) == random.nextInt(zones * 10) ?
                                Condition.DAMAGED : Condition.NOT_DAMAGED;
                default -> condition = Condition.NOT_DAMAGED;
            }
        }
        return condition;
    }

    public static void showPackages() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceImpl().retrieveAll()) {
            PRINTLN.info("id:[" + pack.getId() + "], N:[" + pack.getNumber() + "], PackageType:[" + pack.getPackageType().getTitle() +
                    "], DeliveryType:[" + pack.getDeliveryType().getTitle() + "], Status:[" + pack.getStatus().getTitle() + "], Condition:[" +
                    pack.getCondition().getTitle() + "], Customer:[" + pack.getCustomer().getPersonInfo().getFirstName() + " " +
                    pack.getCustomer().getPersonInfo().getLastName() + "], From:[" + pack.getAddressFrom().getCountry().getTitle() + " - " +
                    pack.getAddressFrom().getCity() + "], To:[" + pack.getAddressTo().getCountry().getTitle() + " - " +
                    pack.getAddressTo().getCity() + "], Cost:[" + Accounting.calculatePackageCost(pack) + " BYN]");
        }
    }
}
