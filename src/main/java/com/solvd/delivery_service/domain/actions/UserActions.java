package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PackageService;
import com.solvd.delivery_service.service.impl.CustomerServiceImpl;
import com.solvd.delivery_service.service.impl.EmployeeServiceImpl;
import com.solvd.delivery_service.service.impl.PackageServiceImpl;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.*;

import java.util.List;
import java.util.Random;

import static com.solvd.delivery_service.util.Printers.*;

public class UserActions extends Actions {

    public static void createPackageWithCreatingCustomer() {
        PRINT2LN.info("REGISTRATION OF A CUSTOMER");
        Passport customerPassport = setPassport();
        Address customerAddress = setAddress();
        PersonInfo customerPersonInfo = setPersonInfo(customerPassport, customerAddress);
        Customer customer = setCustomer(customerPersonInfo);
        Employee employee = setRandomEmployee();
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
    }

    public static void createPackageWithExistCustomer() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        Employee employee = setRandomEmployee();
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
    }

    private static Package registerPackage(Customer customer, Employee employee) {
        PRINT2LN.info("REGISTRATION OF A PACKAGE");
        PackageService packageService = new PackageServiceImpl();
        Long number = packageService.retrieveMaxPackageNumber() + 1;
        PackageType pType = setPackageType();
        DeliveryType delivery = setDeliveryType();
        Address addressTo = setAddress();
        int zones = Accounting.calculateZones(customer.getPersonInfo().getAddress(), addressTo);
        Status status = setRandomStatus(delivery, zones);
        Condition condition = setRandomCondition(status, delivery, zones);
        Package pack = new Package(number, pType, delivery, status, condition, customer.getPersonInfo().getAddress(),
                addressTo, customer, employee);
        if (customer.getId() != null) {
            packageService.createWithExistCustomer(pack);
        } else {
            packageService.create(pack);
        }
        return pack;
    }

    private static Customer getCustomerByLastName() {
        List<Customer> customers;
        String customerLastName;
        do {
            do {
                try {
                    customerLastName = RequestMethods.requestingInfoString("Enter customer last name: ");
                    break;
                } catch (EmptyInputException | StringFormatException e) {
                    LOGGER.error(e.getMessage());
                }
            } while (true);
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
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter number of customer: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return customers.get(answer - 1);
    }

    private static Customer setCustomer(PersonInfo personInfo) {
        return new Customer(personInfo);
    }

    private static Employee setRandomEmployee() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        Random random = new Random();
        return employeeService.retrieveAll().get(random.nextInt(employeeService.retrieveAll().size()));
    }

    private static PackageType setPackageType() {
        double weight;
        do {
            try {
                weight = setWeight();
                break;
            } catch (WeightException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        for (PackageType p_type : PackageType.values()) {
            if (weight <= p_type.getMaxWeight()) return p_type;
        }
        return null;
    }

    private static double setWeight() throws WeightException {
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

    private static DeliveryType setDeliveryType() {
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
        return DeliveryType.values()[answer - 1];
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
}
