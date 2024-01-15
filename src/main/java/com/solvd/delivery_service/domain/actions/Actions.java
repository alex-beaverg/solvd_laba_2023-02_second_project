package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.BlackAccounting;
import com.solvd.delivery_service.domain.accounting.IAccounting;
import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.human.employee.Experience;
import com.solvd.delivery_service.domain.human.employee.Position;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.*;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;
import com.solvd.delivery_service.util.custom_exceptions.*;
import com.solvd.delivery_service.util.functional_interfaces.IPrintAsMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

import static com.solvd.delivery_service.util.Printers.*;

public class Actions {
    protected static final Logger LOGGER = LogManager.getLogger(Actions.class);
    protected static final IPrintAsMenu<Integer, String> printAsMenu = (index, line) -> PRINTLN.info("[" + index + "]: " + line);
    protected static IAccounting accounting = new BlackAccounting(new Accounting());

    public static void showNumberOfDatabaseEntries() {
        PRINT2LN.info("NUMBER OF TABLE ENTRIES IN DATABASE:");
        PRINTLN.info(String.format("%-18s %3d entries", "Addresses table:", new AddressServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Customers table:", new CustomerServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Companies table:", new CompanyServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Departments table:", new DepartmentServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Employees table:", new EmployeeServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Packages table:", new PackageServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Passports table:", new PassportServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format("%-18s %3d entries", "Persons table:", new PersonInfoServiceImpl().retrieveNumberOfEntries()));
    }

    public static void createPackageWithExistingCustomer() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info(String.format("PACKAGE N%d WAS CREATED", pack.getNumber()));
        PRINTLN.info(String.format("PACKAGE COST: %s BYN", accounting.calculatePackageCost(pack)));
    }

    public static void showCustomerPackages() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        String firstName = customer.getPersonInfo().getFirstName();
        String lastName = customer.getPersonInfo().getLastName();
        PRINT2LN.info(String.format("CUSTOMER %s %s PACKAGES:", firstName, lastName));
        PackageService packageService = new PackageServiceImpl();
        for (Package pack : packageService.retrieveCustomerPackages(customer)) {
            PRINTLN.info(String.format("\t- %s, Cost:[%s BYN]", pack, accounting.calculatePackageCost(pack)));
        }
    }

    protected static Address getAddressFromConsole() {
        Address address = new Address();
        address.setCountry((Country) getEnumValueFromConsole(Country.values(), "country"));
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

    protected static IMenu getEnumValueFromConsole(IMenu[] enumArray, String name) {
        int index = 1;
        PRINTLN.info("Choose the " + name + ":");
        for (IMenu item : enumArray) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        return enumArray[RequestMethods.getNumberFromChoice(name + " number", index - 1) - 1];
    }

    protected static Employee getRandomEmployeeFromDataBase(Department department) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        Random random = new Random();
        int index = random.nextInt(employeeService.retrieveDepartmentEmployees(department).size());
        return employeeService.retrieveDepartmentEmployees(department).get(index);
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

    protected static Department getExistingDepartment() {
        DepartmentService departmentService = new DepartmentServiceImpl();
        List<Department> departments = departmentService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the department:");
        for (Department item : departments) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        return departments.get(RequestMethods.getNumberFromChoice("department number", index - 1) - 1);
    }

    protected static Company getExistingCompany() {
        CompanyService companyService = new CompanyServiceImpl();
        List<Company> companies = companyService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the company:");
        for (Company item : companies) {
            printAsMenu.print(index, item.getName());
            index++;
        }
        return companies.get(RequestMethods.getNumberFromChoice("company number", index - 1) - 1);
    }

    protected static Field getAnyClassFieldFromConsole(Class<?> clazz) {
        int index = 1;
        PRINTLN.info("Choose the field:");
        List<Field> allFields = List.of(clazz.getDeclaredFields());
        List<Field> fields = new ArrayList<>();
        for (Field item : allFields) {
            if (!item.getName().equals("id")) {
                printAsMenu.print(index, item.getName());
                fields.add(item);
                index++;
            }
        }
        return fields.get(RequestMethods.getNumberFromChoice("field number", index - 1) - 1);
    }

    protected static Field getEmployeeClassFieldFromConsole() {
        int index = 1;
        PRINTLN.info("Choose the field:");
        List<Field> allEmployeeFields = List.of(Employee.class.getDeclaredFields());
        List<Field> employeeFields = new ArrayList<>();
        for (Field employeeField : allEmployeeFields) {
            if (!employeeField.getName().equals("id") && !employeeField.getName().equals("packages")) {
                printAsMenu.print(index, employeeField.getName());
                employeeFields.add(employeeField);
                index++;
            }
        }
        return employeeFields.get(RequestMethods.getNumberFromChoice("field number", index - 1) - 1);
    }

    protected static Field getDepartmentClassFieldFromConsole() {
        int index = 1;
        PRINTLN.info("Choose the field:");
        List<Field> allDepartmentFields = List.of(Department.class.getDeclaredFields());
        List<Field> departmentFields = new ArrayList<>();
        for (Field departmentField : allDepartmentFields) {
            if (!departmentField.getName().equals("id") && !departmentField.getName().equals("employees")) {
                printAsMenu.print(index, departmentField.getName());
                departmentFields.add(departmentField);
                index++;
            }
        }
        return departmentFields.get(RequestMethods.getNumberFromChoice("field number", index - 1) - 1);
    }

    protected static Map<String, String> switchAddressField(Field addressField, Address address) {
        Map<String, String> values = new HashMap<>();
        switch (addressField.getName()) {
            case ("city") -> {
                values.put("old", address.getCity());
                address.setCity(RequestMethods.getStringValueFromConsole(addressField.getName()));
                values.put("new", address.getCity());
            }
            case ("street") -> {
                values.put("old", address.getStreet());
                address.setStreet(RequestMethods.getStringValueFromConsole(addressField.getName()));
                values.put("new", address.getStreet());
            }
            case ("house") -> {
                values.put("old", String.valueOf(address.getHouse()));
                address.setHouse(RequestMethods.getIntegerValueFromConsole(addressField.getName()));
                values.put("new", String.valueOf(address.getHouse()));
            }
            case ("flat") -> {
                values.put("old", String.valueOf(address.getFlat()));
                address.setFlat(RequestMethods.getIntegerValueFromConsole(addressField.getName()));
                values.put("new", String.valueOf(address.getFlat()));
            }
            case ("zipCode") -> {
                values.put("old", String.valueOf(address.getZipCode()));
                address.setZipCode(RequestMethods.getIntegerValueFromConsole("zip code"));
                values.put("new", String.valueOf(address.getZipCode()));
            }
            case ("country") -> {
                values.put("old", address.getCountry().getTitle());
                address.setCountry((Country) getEnumValueFromConsole(Country.values(), addressField.getName()));
                values.put("new", address.getCountry().getTitle());
            }
        }
        return values;
    }

    protected static Map<String, String> switchPersonInfoField(Field personInfoField, PersonInfo personInfo) {
        PassportService passportService = new PassportServiceImpl();
        AddressService addressService = new AddressServiceImpl();
        Map<String, String> values = new HashMap<>();
        switch (personInfoField.getName()) {
            case ("firstName") -> {
                values.put("old", personInfo.getFirstName());
                personInfo.setFirstName(RequestMethods.getStringValueFromConsole("first name"));
                values.put("new", personInfo.getFirstName());
            }
            case ("lastName") -> {
                values.put("old", personInfo.getLastName());
                personInfo.setLastName(RequestMethods.getStringValueFromConsole("last name"));
                values.put("new", personInfo.getLastName());
            }
            case ("age") -> {
                values.put("old", String.valueOf(personInfo.getAge()));
                personInfo.setAge(RequestMethods.getIntegerValueFromConsole(personInfoField.getName()));
                values.put("new", String.valueOf(personInfo.getAge()));
            }
            case ("passport") -> {
                Passport passport = personInfo.getPassport();
                values.put("old", passport.getNumber());
                passport.setNumber(RequestMethods.getStringValueFromConsole("passport number"));
                values.put("new", passport.getNumber());
                passportService.updateField(passport);
            }
            case ("address") -> {
                Address employeeAddress = personInfo.getAddress();
                Field addressField = getAnyClassFieldFromConsole(Address.class);
                values = switchAddressField(addressField, employeeAddress);
                addressService.updateField(employeeAddress, addressField.getName());
            }
        }
        return values;
    }

    protected static Map<String, String> switchEmployeeField(Field employeeField, Employee employee) {
        DepartmentService departmentService = new DepartmentServiceImpl();
        CompanyService companyService = new CompanyServiceImpl();
        PersonInfoService personInfoService = new PersonInfoServiceImpl();
        Map<String, String> values = new HashMap<>();
        switch (employeeField.getName()) {
            case ("position") -> {
                values.put("old", employee.getPosition().getTitle());
                employee.setPosition((Position) getEnumValueFromConsole(Position.values(), employeeField.getName()));
                values.put("new", employee.getPosition().getTitle());
            }
            case ("experience") -> {
                values.put("old", employee.getExperience().getTitle());
                employee.setExperience((Experience) getEnumValueFromConsole(Experience.values(), employeeField.getName()));
                values.put("new", employee.getExperience().getTitle());
            }
            case ("department") -> {
                Department department = employee.getDepartment();
                Field departmentField = getDepartmentClassFieldFromConsole();
                switch (departmentField.getName()) {
                    case ("title") -> {
                        values.put("old", department.getTitle());
                        department.setTitle(RequestMethods.getStringValueFromConsole(departmentField.getName()));
                        values.put("new", department.getTitle());
                    }
                    case ("company") -> {
                        Company company = department.getCompany();
                        values.put("old", company.getName());
                        company.setName(RequestMethods.getStringValueFromConsole("company name"));
                        values.put("new", company.getName());
                        companyService.updateField(company);
                    }
                }
                if (departmentField.getName().equals("title")) {
                    departmentService.updateField(department);
                }
            }
            case ("personInfo") -> {
                PersonInfo personInfo = employee.getPersonInfo();
                Field personInfoField = getAnyClassFieldFromConsole(PersonInfo.class);
                values = switchPersonInfoField(personInfoField, personInfo);
                if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                    personInfoService.updateField(personInfo, personInfoField.getName());
                }
            }
        }
        return values;
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

    private static PackageType getPackageTypeDependingOnWeight() {
        double weight;
        do {
            try {
                weight = getPackageWeightFromConsole();
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

    private static double getPackageWeightFromConsole() throws PackageWeightException {
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

    private static Customer getCustomerByLastName() {
        List<Customer> customers;
        String customerLastName;
        do {
            customerLastName = RequestMethods.getStringValueFromConsole("customer last name");
            customers = new CustomerServiceImpl().retrieveEntriesByLastName(customerLastName);
            if (customers.size() == 0) {
                PRINTLN.info(String.format("[Info]: Customer with last name %s doesn't exist!", customerLastName));
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
}