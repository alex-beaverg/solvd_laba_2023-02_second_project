package com.solvd.delivery_service.domain.actions.entity;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.service.AddressService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PackageService;
import com.solvd.delivery_service.service.PersonInfoService;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.solvd.delivery_service.util.Printers.*;

public class PackageActions extends Actions implements IEntityActions {

    @Override
    public void showEntityEntries() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceImpl().retrieveAll()) {
            PRINTLN.info(String.format("%s, Cost:[%s BYN]", pack, accounting.calculatePackageCost(pack)));
        }
    }

    @Override
    public void registerEntityEntry() {
        PRINT2LN.info("REGISTRATION OF A CUSTOMER");
        Passport customerPassport = new Passport(RequestMethods.getStringValueFromConsole("passport number"));
        Address customerAddress = getAddressFromConsole();
        PersonInfo customerPersonInfo = getPersonInfoFromConsole(customerPassport, customerAddress);
        Customer customer = new Customer(customerPersonInfo);
        Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
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
        Field packageField = getAnyClassFieldFromConsole(Package.class);
        String oldValue = "", newValue = "";
        switch (packageField.getName()) {
            case ("number") -> {
                oldValue = String.valueOf(pack.getNumber());
                pack.setNumber(RequestMethods.getLongValueFromConsole(packageField.getName()));
                newValue = String.valueOf(pack.getNumber());
            }
            case ("packageType") -> {
                oldValue = pack.getPackageType().getTitle();
                pack.setPackageType((PackageType) getEnumValueFromConsole(PackageType.values(), "package type"));
                newValue = pack.getPackageType().getTitle();
            }
            case ("deliveryType") -> {
                oldValue = pack.getDeliveryType().getTitle();
                pack.setDeliveryType((DeliveryType) getEnumValueFromConsole(DeliveryType.values(), "delivery type"));
                newValue = pack.getDeliveryType().getTitle();
            }
            case ("status") -> {
                oldValue = pack.getStatus().getTitle();
                pack.setStatus((Status) getEnumValueFromConsole(Status.values(), packageField.getName()));
                newValue = pack.getStatus().getTitle();
            }
            case ("condition") -> {
                oldValue = pack.getCondition().getTitle();
                pack.setCondition((Condition) getEnumValueFromConsole(Condition.values(), packageField.getName()));
                newValue = pack.getCondition().getTitle();
            }
            case ("addressFrom") -> {
                Address addressFrom = pack.getAddressFrom();
                Field addressField = getAnyClassFieldFromConsole(Address.class);
                Map<String, String> values = switchAddressField(addressField, addressFrom);
                oldValue = values.get("old");
                newValue = values.get("new");
                addressService.updateField(addressFrom, addressField.getName());
            }
            case ("addressTo") -> {
                Address addressTo = pack.getAddressTo();
                Field addressField = getAnyClassFieldFromConsole(Address.class);
                Map<String, String> values = switchAddressField(addressField, addressTo);
                oldValue = values.get("old");
                newValue = values.get("new");
                addressService.updateField(addressTo, addressField.getName());
            }
            case ("customer") -> {
                PersonInfo personInfo = pack.getCustomer().getPersonInfo();
                Field personInfoField = getAnyClassFieldFromConsole(PersonInfo.class);
                Map<String, String> values = switchPersonInfoField(personInfoField, personInfo);
                oldValue = values.get("old");
                newValue = values.get("new");
                if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                    personInfoService.updateField(personInfo, personInfoField.getName());
                }
            }
            case ("employee") -> {
                Employee employee = pack.getEmployee();
                Field employeeField = getEmployeeClassFieldFromConsole();
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
}
