package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.CustomerService;
import com.solvd.delivery_service.service.DepartmentService;
import com.solvd.delivery_service.service.PackageService;
import com.solvd.delivery_service.service.impl.CustomerServiceDaoImpl;
import com.solvd.delivery_service.service.impl.DepartmentServiceDaoImpl;
import com.solvd.delivery_service.service.impl.PackageServiceDaoImpl;
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
        Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceDaoImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
    }

    public static void createPackageWithExistingCustomer() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceDaoImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
        PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
    }

    public static void showCustomerPackages() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        PRINT2LN.info("CUSTOMER " + customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName() + " PACKAGES:");
        CustomerService customerService = new CustomerServiceDaoImpl();
        for (Package pack : customerService.retrieveCustomerPackages(customer)) {
            PRINTLN.info("id:[" +
                    pack.getId() + "], N:[" +
                    pack.getNumber() + "], Package:[" +
                    pack.getPackageType().getTitle() + "], Delivery:[" +
                    pack.getDeliveryType().getTitle() + "], Status:[" +
                    pack.getStatus().getTitle() + "], Condition:[" +
                    pack.getCondition().getTitle() + "], From:[" +
                    pack.getAddressFrom().getCountry().getTitle() + "/" +
                    pack.getAddressFrom().getCity() + "], To:[" +
                    pack.getAddressTo().getCountry().getTitle() + "/" +
                    pack.getAddressTo().getCity() + "], Employee:[" +
                    pack.getEmployee().getPersonInfo().getFirstName() + " " +
                    pack.getEmployee().getPersonInfo().getLastName() + "], Cost:[" +
                    Accounting.calculatePackageCost(pack) + " BYN]");
        }
    }

    private static Package registerPackage(Customer customer, Employee employee) {
        PRINT2LN.info("REGISTRATION OF A PACKAGE");
        PackageService packageService = new PackageServiceDaoImpl();
        Long number = packageService.retrieveMaxPackageNumber() + 1;
        PackageType pType = getPackageTypeDependingOnWeight();
        DeliveryType delivery = (DeliveryType) getEnumFromConsole(DeliveryType.values(), "delivery type");
        Address addressTo = getAddressFromConsole();
        int zones = Accounting.calculateZones(customer.getPersonInfo().getAddress(), addressTo);
        Status status = getRandomStatus(delivery, zones);
        Condition condition = getRandomCondition(status, delivery, zones);
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
            customerLastName = RequestMethods.getStringValueFromConsole("customer last name");
            customers = new CustomerServiceDaoImpl().retrieveEntriesByLastName(customerLastName);
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

    private static Employee getRandomEmployeeFromDataBase(Department department) {
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        Random random = new Random();
        return departmentService.retrieveDepartmentEmployees(department)
                .get(random.nextInt(departmentService.retrieveDepartmentEmployees(department).size()));
    }

    private static PackageType getPackageTypeDependingOnWeight() {
        double weight;
        do {
            try {
                weight = getWeightFromConsole();
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

    private static Status getRandomStatus(DeliveryType deliveryType, int zones) {
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

    private static Condition getRandomCondition(Status status, DeliveryType deliveryType, int zones) {
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
