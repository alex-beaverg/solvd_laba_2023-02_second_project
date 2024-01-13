package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.*;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.*;

import java.util.List;
import java.util.Random;

import static com.solvd.delivery_service.util.Printers.*;

public class UserActions extends Actions {

    public static void createPackageWithRegistrationNewCustomer() {
        PRINT2LN.info("REGISTRATION OF A CUSTOMER");
        Passport customerPassport = getPassportFromConsole();
        Address customerAddress = getAddressFromConsole();
        PersonInfo customerPersonInfo = getPersonInfoFromConsole(customerPassport, customerAddress);
        Customer customer = getCustomerWithPersonInfo(customerPersonInfo);
        Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + accounting.calculatePackageCost(pack) + " BYN");
    }

    public static void createPackageWithExistingCustomer() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + accounting.calculatePackageCost(pack) + " BYN");
    }

    public static void showCustomerPackages() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        PRINT2LN.info("CUSTOMER " + customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName() + " PACKAGES:");
        PackageService packageService = new PackageServiceImpl();
        for (Package pack : packageService.retrieveCustomerPackages(customer)) {
            PRINTLN.info("\t- " + pack + ", Cost:[" + accounting.calculatePackageCost(pack) + " BYN]");
        }
    }

    protected static Package registerPackage(Customer customer, Employee employee) {
        PRINT2LN.info("REGISTRATION OF A PACKAGE");
        PackageService packageService = new PackageServiceImpl();
        Long number = packageService.retrieveMaxPackageNumber() + 1;
        PackageType pType = getPackageTypeDependingOnWeight();
        DeliveryType delivery = (DeliveryType) getEnumValueFromConsole(DeliveryType.values(), "delivery type");
        Address addressTo = getAddressFromConsole();
        int zones = accounting.calculateZones(customer.getPersonInfo().getAddress(), addressTo);
        int daysDuration = zones * delivery.getDaysCountPerZone();
        Status status = getRandomStatus(delivery, daysDuration);
        Condition condition = getRandomCondition(status, delivery, daysDuration);
        Package pack = new Package(number, pType, delivery, status, condition, customer.getPersonInfo().getAddress(),
                addressTo, customer, employee);
        if (customer.getId() != null) {
            packageService.createWithExistingCustomer(pack);
        } else {
            packageService.create(pack);
        }
        return pack;
    }

    private static Customer getCustomerByLastName() {
        List<Customer> customers;
        String customerLastName;
        do {
            customerLastName = RequestMethods.getStringValueFromConsole("customer last name");
            customers = new CustomerServiceImpl().retrieveEntriesByLastName(customerLastName);
            if (customers.size() == 0) {
                PRINTLN.info("[Info]: Customer with last name " + customerLastName + " doesn't exist!");
            }
        } while (customers.size() == 0);
        int index = 1;
        PRINTLN.info("Choose the customer:");
        for (Customer item : customers) {
            printAsMenu.print(index, item.getPersonInfo().getFirstName() + " " + item.getPersonInfo().getLastName());
            index++;
        }
        return customers.get(RequestMethods.getNumberFromChoice("customer number", index - 1) - 1);
    }

    private static Customer getCustomerWithPersonInfo(PersonInfo personInfo) {
        return new Customer(personInfo);
    }

    protected static Employee getRandomEmployeeFromDataBase(Department department) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        Random random = new Random();
        int index = random.nextInt(employeeService.retrieveDepartmentEmployees(department).size());
        return employeeService.retrieveDepartmentEmployees(department).get(index);
    }

    private static PackageType getPackageTypeDependingOnWeight() {
        double weight;
        do {
            try {
                weight = getWeightFromConsole();
                break;
            } catch (PackageWeightException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        for (PackageType p_type : PackageType.values()) {
            if (weight <= p_type.getMaxWeight()) return p_type;
        }
        return null;
    }

    private static double getWeightFromConsole() throws PackageWeightException {
        do {
            try {
                double weight = RequestMethods.requestingInfoDouble("Enter package weight in kg (0...100): ");
                if (weight > 100.0) {
                    throw new PackageWeightException("[WeightException]: Weight can not be more than 100 kg!");
                }
                return weight;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
    }

    private static Status getRandomStatus(DeliveryType deliveryType, int daysDuration) {
        Status status;
        Random random = new Random();
        switch (deliveryType.name()) {
            case ("REGULAR") -> status =
                    random.nextInt(daysDuration * 2) == random.nextInt(daysDuration * 2) ?
                            Status.LOST : Status.DELIVERED;
            case ("EXPRESS") -> status =
                    random.nextInt(daysDuration * 10) == random.nextInt(daysDuration * 10) ?
                            Status.LOST : Status.DELIVERED;
            default -> status = Status.DELIVERED;
        }
        return status;
    }

    private static Condition getRandomCondition(Status status, DeliveryType deliveryType, int daysDuration) {
        Condition condition;
        Random random = new Random();
        if (status == Status.LOST) {
            condition = Condition.UNKNOWN;
        } else {
            switch (deliveryType.name()) {
                case ("REGULAR") -> condition =
                        random.nextInt(daysDuration * 2) == random.nextInt(daysDuration * 2) ?
                                Condition.DAMAGED : Condition.NOT_DAMAGED;
                case ("EXPRESS") -> condition =
                        random.nextInt(daysDuration * 10) == random.nextInt(daysDuration * 10) ?
                                Condition.DAMAGED : Condition.NOT_DAMAGED;
                default -> condition = Condition.NOT_DAMAGED;
            }
        }
        return condition;
    }
}