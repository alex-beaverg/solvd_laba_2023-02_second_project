package com.solvd.delivery_service.domain.actions.entity_actions;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.actions.console_actions.ClassInfoActions;
import com.solvd.delivery_service.domain.actions.console_actions.DataInfoActions;
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
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.NegativeNumberException;
import com.solvd.delivery_service.util.custom_exceptions.PackageWeightException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.solvd.delivery_service.util.Printers.*;

public class PackageActions extends Actions implements IEntityActions {

    @Override
    public void showEntityEntries() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceImpl().retrieveAll()) {
            PRINTLN.info(String.format("- %s, Cost:[%s BYN]", pack, accounting.calculatePackageCost(pack)));
        }
    }

    @Override
    public void registerEntityEntry() {
        PRINT2LN.info("REGISTRATION OF A CUSTOMER");
        Passport customerPassport = new Passport(RequestMethods.getStringValueFromConsole("passport number"));
        Address customerAddress = DataInfoActions.getAddressFromConsole();
        PersonInfo customerPersonInfo = DataInfoActions.getPersonInfoFromConsole(customerPassport, customerAddress);
        Customer customer = new Customer(customerPersonInfo);
        Employee employee = EmployeeActions.getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info(String.format("PACKAGE N%d WAS CREATED", pack.getNumber()));
        PRINTLN.info(String.format("PACKAGE COST: %s BYN", accounting.calculatePackageCost(pack)));
    }

    @Override
    public void removeEntityEntry() {
        PRINT2LN.info("REMOVING PACKAGE");
        PackageService packageService = new PackageServiceImpl();
        List<Package> packages = packageService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the package:");
        for (Package pack : packages) {
            printAsMenu.print(index, String.format("Package N%d from %s to %s",
                    pack.getNumber(),
                    pack.getAddressFrom().getCountry().getTitle(),
                    pack.getAddressTo().getCountry().getTitle()));
            index++;
        }
        Package pack = packages.get(RequestMethods.getNumberFromChoice("package [number]", index - 1) - 1);
        Long package_id = pack.getId();
        packageService.removeById(package_id);
        PRINT2LN.info(String.format("PACKAGE N%d WAS REMOVED", pack.getNumber()));
    }

    @Override
    public void updateEntityEntryField() {
        PRINT2LN.info("UPDATING PACKAGE");
        PackageService packageService = new PackageServiceImpl();
        AddressService addressService = new AddressServiceImpl();
        PersonInfoService personInfoService = new PersonInfoServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();
        Package pack = getExistingPackage();
        Field packageField = ClassInfoActions.getAnyClassFieldFromConsole(Package.class);
        String oldValue = "", newValue = "";
        switch (packageField.getName()) {
            case ("number") -> {
                oldValue = String.valueOf(pack.getNumber());
                pack.setNumber(RequestMethods.getLongValueFromConsole(packageField.getName()));
                newValue = String.valueOf(pack.getNumber());
            }
            case ("packageType") -> {
                oldValue = pack.getPackageType().getTitle();
                pack.setPackageType((PackageType) DataInfoActions.chooseEnumValueFromConsole(PackageType.values(), "package type"));
                newValue = pack.getPackageType().getTitle();
            }
            case ("deliveryType") -> {
                oldValue = pack.getDeliveryType().getTitle();
                pack.setDeliveryType((DeliveryType) DataInfoActions.chooseEnumValueFromConsole(DeliveryType.values(), "delivery type"));
                newValue = pack.getDeliveryType().getTitle();
            }
            case ("status") -> {
                oldValue = pack.getStatus().getTitle();
                pack.setStatus((Status) DataInfoActions.chooseEnumValueFromConsole(Status.values(), packageField.getName()));
                newValue = pack.getStatus().getTitle();
            }
            case ("condition") -> {
                oldValue = pack.getCondition().getTitle();
                pack.setCondition((Condition) DataInfoActions.chooseEnumValueFromConsole(Condition.values(), packageField.getName()));
                newValue = pack.getCondition().getTitle();
            }
            case ("addressFrom") -> {
                Address addressFrom = pack.getAddressFrom();
                Field addressField = ClassInfoActions.getAnyClassFieldFromConsole(Address.class);
                Map<String, String> values = switchAddressField(addressField, addressFrom);
                oldValue = values.get("old");
                newValue = values.get("new");
                addressService.updateField(addressFrom, addressField.getName());
            }
            case ("addressTo") -> {
                Address addressTo = pack.getAddressTo();
                Field addressField = ClassInfoActions.getAnyClassFieldFromConsole(Address.class);
                Map<String, String> values = switchAddressField(addressField, addressTo);
                oldValue = values.get("old");
                newValue = values.get("new");
                addressService.updateField(addressTo, addressField.getName());
            }
            case ("customer") -> {
                PersonInfo personInfo = pack.getCustomer().getPersonInfo();
                Field personInfoField = ClassInfoActions.getAnyClassFieldFromConsole(PersonInfo.class);
                Map<String, String> values = switchPersonInfoField(personInfoField, personInfo);
                oldValue = values.get("old");
                newValue = values.get("new");
                if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                    personInfoService.updateField(personInfo, personInfoField.getName());
                }
            }
            case ("employee") -> {
                Employee employee = pack.getEmployee();
                Field employeeField = ClassInfoActions.getEmployeeClassFieldFromConsole();
                Map<String, String> values = switchEmployeeField(employeeField, employee);
                oldValue = values.get("old");
                newValue = values.get("new");
                if (employeeField.getName().equals("position") || employeeField.getName().equals("experience")) {
                    employeeService.updateField(employee, employeeField.getName());
                }
            }
        }
        if (packageField.getName().equals("number") || packageField.getName().equals("packageType") ||
                packageField.getName().equals("deliveryType") || packageField.getName().equals("status") ||
                packageField.getName().equals("condition")) {
            packageService.updateField(pack, packageField.getName());
        }
        PRINT2LN.info(String.format("PACKAGE N%d FIELD %s WAS UPDATED FROM %s TO %s",
                pack.getNumber(), packageField.getName(), oldValue, newValue));
    }

    public static void createPackageWithExistingCustomer() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = CustomerActions.getCustomerByLastName();
        Employee employee = EmployeeActions.getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
        Package pack = registerPackage(customer, employee);
        PRINT2LN.info(String.format("PACKAGE N%d WAS CREATED", pack.getNumber()));
        PRINTLN.info(String.format("PACKAGE COST: %s BYN", accounting.calculatePackageCost(pack)));
    }

    public static Package registerPackage(Customer customer, Employee employee) {
        PRINT2LN.info("REGISTRATION OF A PACKAGE");
        PackageService packageService = new PackageServiceImpl();
        Long number = packageService.retrieveMaxPackageNumber() + 1;
        PackageType pType = getPackageTypeDependingOnWeight();
        DeliveryType delivery = (DeliveryType) DataInfoActions.chooseEnumValueFromConsole(DeliveryType.values(), "delivery type");
        Address addressTo = DataInfoActions.getAddressFromConsole();
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
                address.setCountry((Country) DataInfoActions.chooseEnumValueFromConsole(Country.values(), addressField.getName()));
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
                Field addressField = ClassInfoActions.getAnyClassFieldFromConsole(Address.class);
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
                employee.setPosition((Position) DataInfoActions.chooseEnumValueFromConsole(Position.values(), employeeField.getName()));
                values.put("new", employee.getPosition().getTitle());
            }
            case ("experience") -> {
                values.put("old", employee.getExperience().getTitle());
                employee.setExperience((Experience) DataInfoActions.chooseEnumValueFromConsole(Experience.values(), employeeField.getName()));
                values.put("new", employee.getExperience().getTitle());
            }
            case ("department") -> {
                Department department = employee.getDepartment();
                Field departmentField = ClassInfoActions.getDepartmentClassFieldFromConsole();
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
                Field personInfoField = ClassInfoActions.getAnyClassFieldFromConsole(PersonInfo.class);
                values = switchPersonInfoField(personInfoField, personInfo);
                if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                    personInfoService.updateField(personInfo, personInfoField.getName());
                }
            }
        }
        return values;
    }

    private static Package getExistingPackage() {
        PackageService packageService = new PackageServiceImpl();
        List<Package> packages = packageService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the package:");
        for (Package pack : packages) {
            printAsMenu.print(index, String.format("Package N%d from %s to %s",
                    pack.getNumber(),
                    pack.getAddressFrom().getCountry().getTitle(),
                    pack.getAddressTo().getCountry().getTitle()));
            index++;
        }
        return packages.get(RequestMethods.getNumberFromChoice("package [number]", index - 1) - 1);
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
}