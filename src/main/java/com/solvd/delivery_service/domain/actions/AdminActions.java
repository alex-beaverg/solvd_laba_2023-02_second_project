package com.solvd.delivery_service.domain.actions;

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
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.*;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.solvd.delivery_service.util.Printers.*;

public class AdminActions extends Actions {

    public static void showNumberOfEntriesInDb() {
        PRINT2LN.info("NUMBER OF TABLE ENTRIES IN DATABASE:");
        PRINTLN.info("Addresses table: " + new AddressServiceDaoImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Customers table: " + new CustomerServiceDaoImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Departments table: " + new DepartmentServiceDaoImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Employees table: " + new EmployeeServiceDaoImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Packages table: " + new PackageServiceDaoImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Passports table: " + new PassportServiceDaoImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Persons table: " + new PersonInfoServiceDaoImpl().retrieveNumberOfEntries() + " entries");
    }

    public static void showDepartments() {
        PRINT2LN.info("ALL DEPARTMENTS:");
        for (Department department : new DepartmentServiceDaoImpl().retrieveAll()) {
            PRINTLN.info("DEPARTMENT: id:[" +
                    department.getId() + "], Title:[" +
                    department.getTitle() + "]\n\tEMPLOYEES:");
            department.getEmployees().forEach(employee -> PRINTLN.info("\t- id:[" +
                    employee.getId() + "], Name:[" +
                    employee.getPersonInfo().getFirstName() + " " +
                    employee.getPersonInfo().getLastName() + "], Dep:[" +
                    employee.getDepartment().getTitle() + "], Pos:[" +
                    employee.getPosition().getTitle() + "], Exp:[" +
                    employee.getExperience().getTitle() + "], Age:[" +
                    employee.getPersonInfo().getAge() + "], Passport:[" +
                    employee.getPersonInfo().getPassport().getNumber() + "], Address:[" +
                    employee.getPersonInfo().getAddress().getZipCode() + ", " +
                    employee.getPersonInfo().getAddress().getCountry().getTitle() + ", " +
                    employee.getPersonInfo().getAddress().getCity() + ", " +
                    employee.getPersonInfo().getAddress().getStreet() + ", " +
                    employee.getPersonInfo().getAddress().getHouse() + "-" +
                    employee.getPersonInfo().getAddress().getFlat() + "], Salary:[" +
                    Accounting.calculateEmployeeSalary(employee) + " BYN]"));
        }
    }

    public static void showEmployees() {
        PRINT2LN.info("ALL EMPLOYEES:");
        for (Employee employee : new EmployeeServiceDaoImpl().retrieveAll()) {
            PRINTLN.info("EMPLOYEE: id:[" +
                    employee.getId() + "], Name:[" +
                    employee.getPersonInfo().getFirstName() + " " +
                    employee.getPersonInfo().getLastName() + "], Dep:[" +
                    employee.getDepartment().getTitle() + "], Pos:[" +
                    employee.getPosition().getTitle() + "], Exp:[" +
                    employee.getExperience().getTitle() + "], Age:[" +
                    employee.getPersonInfo().getAge() + "], Passport:[" +
                    employee.getPersonInfo().getPassport().getNumber() + "], Address:[" +
                    employee.getPersonInfo().getAddress().getZipCode() + ", " +
                    employee.getPersonInfo().getAddress().getCountry().getTitle() + ", " +
                    employee.getPersonInfo().getAddress().getCity() + ", " +
                    employee.getPersonInfo().getAddress().getStreet() + ", " +
                    employee.getPersonInfo().getAddress().getHouse() + "-" +
                    employee.getPersonInfo().getAddress().getFlat() + "], Salary:[" +
                    Accounting.calculateEmployeeSalary(employee) + " BYN]\n\tPACKAGES:");
            employee.getPackages().forEach(pack -> PRINTLN.info("\t- id:[" +
                    pack.getId() + "], N:[" +
                    pack.getNumber() + "], Package type:[" +
                    pack.getPackageType().getTitle() + "], Delivery type:[" +
                    pack.getDeliveryType() + "], Status:[" +
                    pack.getStatus() + "], Condition:[" +
                    pack.getCondition() + "], From:[" +
                    pack.getAddressFrom().getCountry().getTitle() + "/" +
                    pack.getAddressFrom().getCity() + "], To:[" +
                    pack.getAddressTo().getCountry().getTitle() + "/" +
                    pack.getAddressTo().getCity() + "], Customer:[" +
                    pack.getCustomer().getPersonInfo().getFirstName() + " " +
                    pack.getCustomer().getPersonInfo().getLastName() + "]"));
        }
    }

    public static void showPackages() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceDaoImpl().retrieveAll()) {
            PRINTLN.info("id:[" +
                    pack.getId() + "], N:[" +
                    pack.getNumber() + "], Package:[" +
                    pack.getPackageType().getTitle() + "], Delivery:[" +
                    pack.getDeliveryType().getTitle() + "], Status:[" +
                    pack.getStatus().getTitle() + "], Condition:[" +
                    pack.getCondition().getTitle() + "], Customer:[" +
                    pack.getCustomer().getPersonInfo().getFirstName() + " " +
                    pack.getCustomer().getPersonInfo().getLastName() + "], From:[" +
                    pack.getAddressFrom().getCountry().getTitle() + "/" +
                    pack.getAddressFrom().getCity() + "], To:[" +
                    pack.getAddressTo().getCountry().getTitle() + "/" +
                    pack.getAddressTo().getCity() + "], Employee:[" +
                    pack.getEmployee().getPersonInfo().getFirstName() + " " +
                    pack.getEmployee().getPersonInfo().getLastName() + "], Cost:[" +
                    Accounting.calculatePackageCost(pack) + " BYN]");
        }
    }

    public static void showCustomers() {
        PRINT2LN.info("ALL CUSTOMERS:");
        for (Customer customer : new CustomerServiceDaoImpl().retrieveAll()) {
            PRINTLN.info("CUSTOMER: id:[" +
                    customer.getId() + "], Person:[" +
                    customer.getPersonInfo().getFirstName() + " " +
                    customer.getPersonInfo().getLastName() + "], Age:[" +
                    customer.getPersonInfo().getAge() + "], Passport:[" +
                    customer.getPersonInfo().getPassport().getNumber() + "], Address:[" +
                    customer.getPersonInfo().getAddress().getZipCode() + ", " +
                    customer.getPersonInfo().getAddress().getCountry().getTitle() + ", " +
                    customer.getPersonInfo().getAddress().getCity() + ", " +
                    customer.getPersonInfo().getAddress().getStreet() + ", " +
                    customer.getPersonInfo().getAddress().getHouse() + "-" +
                    customer.getPersonInfo().getAddress().getFlat() + "]\n\tPACKAGES:");
            customer.getPackages().forEach(pack -> PRINTLN.info("\t- id:[" +
                    pack.getId() + "], N:[" +
                    pack.getNumber() + "], Package type:[" +
                    pack.getPackageType().getTitle() + "], Delivery type:[" +
                    pack.getDeliveryType() + "], Status:[" +
                    pack.getStatus() + "], Condition:[" +
                    pack.getCondition() + "], From:[" +
                    pack.getAddressFrom().getCountry().getTitle() + "/" +
                    pack.getAddressFrom().getCity() + "], To:[" +
                    pack.getAddressTo().getCountry().getTitle() + "/" +
                    pack.getAddressTo().getCity() + "], Employee:[" +
                    pack.getEmployee().getPersonInfo().getFirstName() + " " +
                    pack.getEmployee().getPersonInfo().getLastName() + "]"));
        }
    }

    public static void registerEmployee() {
        PRINT2LN.info("REGISTERING EMPLOYEE");
        Passport employeePassport = getPassportFromConsole();
        Address employeeAddress = getAddressFromConsole();
        PersonInfo employeePersonInfo = getPersonInfoFromConsole(employeePassport, employeeAddress);
        Position employeePosition = (Position) getEnumFromConsole(Position.values(), "position");
        Experience employeeExperience = (Experience) getEnumFromConsole(Experience.values(), "experience");
        Department employeeDepartment = getExistingDepartment();
        Employee employee = new Employee(employeePosition, employeeExperience, employeeDepartment, employeePersonInfo);
        EmployeeService employeeService = new EmployeeServiceDaoImpl();
        employeeService.createWithExistDepartment(employee);
        PRINT2LN.info("EMPLOYEE " + employeePersonInfo.getFirstName() + " " + employeePersonInfo.getLastName() + " WAS REGISTERED");
        PRINT2LN.info("EMPLOYEE SALARY: " + Accounting.calculateEmployeeSalary(employee) + " BYN");
    }

    public static void registerDepartment() {
        PRINT2LN.info("REGISTERING DEPARTMENT");
        Department department = new Department(RequestMethods.getStringValueFromConsole("department title"));
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        departmentService.create(department);
        PRINT2LN.info("DEPARTMENT " + department.getTitle() + " WAS REGISTERED");
    }

    public static void removeDepartment() {
        PRINT2LN.info("REMOVING DEPARTMENT");
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        Department department = getExistingDepartment();
        Long department_id = department.getId();
        departmentService.removeById(department_id);
        PRINT2LN.info("DEPARTMENT " + department.getTitle() + " WAS REMOVED");
    }

    public static void renameDepartment() {
        PRINT2LN.info("RENAMING DEPARTMENT");
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        Department department = getExistingDepartment();
        String newDepartmentTitle = RequestMethods.getStringValueFromConsole("new department title");
        String oldDepartmentTitle = department.getTitle();
        department.setTitle(newDepartmentTitle);
        departmentService.rename(department);
        PRINT2LN.info("DEPARTMENT " + oldDepartmentTitle + " WAS RENAMED TO " + newDepartmentTitle);
    }

    public static void removeCustomer() {
        PRINT2LN.info("REMOVING CUSTOMER");
        CustomerService customerService = new CustomerServiceDaoImpl();
        Customer customer = getExistingCustomer();
        Long customer_id = customer.getId();
        customerService.removeById(customer_id);
        PRINT2LN.info("CUSTOMER " + customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName() + " WAS REMOVED");
    }

    public static void updateCustomerField() {

    }

    public static void removeEmployee() {
        PRINT2LN.info("REMOVING EMPLOYEE");
        EmployeeService employeeService = new EmployeeServiceDaoImpl();
        Employee employee = getExistingEmployee();
        Long employee_id = employee.getId();
        employeeService.removeById(employee_id);
        PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() + " WAS REMOVED");
    }

    public static void updateEmployeeField() {
        PRINT2LN.info("UPDATING EMPLOYEE");
        EmployeeService employeeService = new EmployeeServiceDaoImpl();
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        PersonInfoService personInfoService = new PersonInfoServiceDaoImpl();
        PassportService passportService = new PassportServiceDaoImpl();
        AddressService addressService = new AddressServiceDaoImpl();
        Employee employee = getExistingEmployee();
        Field employeeField = getEmployeeClassFieldFromConsole();
        String oldValue = "", newValue = "";
        switch (employeeField.getName()) {
            case ("position") -> {
                oldValue = employee.getPosition().getTitle();
                employee.setPosition((Position) getEnumFromConsole(Position.values(), "position"));
                newValue = employee.getPosition().getTitle();
            }
            case ("experience") -> {
                oldValue = employee.getExperience().getTitle();
                employee.setExperience((Experience) getEnumFromConsole(Experience.values(), "experience"));
                newValue = employee.getExperience().getTitle();
            }
            case ("department") -> {
                Department department = employee.getDepartment();
                oldValue = department.getTitle();
                department.setTitle(RequestMethods.getStringValueFromConsole(employeeField.getName()));
                newValue = department.getTitle();
                departmentService.rename(department);
            }
            case ("personInfo") -> {
                PersonInfo personInfo = employee.getPersonInfo();
                Field personInfoField = getAnyClassFieldFromConsole(PersonInfo.class);
                switch (personInfoField.getName()) {
                    case ("firstName") -> {
                        oldValue = personInfo.getFirstName();
                        personInfo.setFirstName(RequestMethods.getStringValueFromConsole("first name"));
                        newValue = personInfo.getFirstName();
                    }
                    case ("lastName") -> {
                        oldValue = personInfo.getLastName();
                        personInfo.setLastName(RequestMethods.getStringValueFromConsole("last name"));
                        newValue = personInfo.getLastName();
                    }
                    case ("age") -> {
                        oldValue = String.valueOf(personInfo.getAge());
                        personInfo.setAge(RequestMethods.getIntegerValueFromConsole(personInfoField.getName()));
                        newValue = String.valueOf(personInfo.getAge());
                    }
                    case ("passport") -> {
                        Passport passport = employee.getPersonInfo().getPassport();
                        oldValue = passport.getNumber();
                        passport.setNumber(RequestMethods.getStringValueFromConsole("passport number"));
                        newValue = passport.getNumber();
                        passportService.updateField(passport);
                    }
                    case ("address") -> {
                        Address employeeAddress = employee.getPersonInfo().getAddress();
                        Field addressField = getAnyClassFieldFromConsole(Address.class);
                        List<String> values = switchAddressField(addressField, employeeAddress);
                        oldValue = values.get(0);
                        newValue = values.get(1);
                        addressService.updateField(employeeAddress, addressField.getName());
                    }
                }
                if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                    personInfoService.updateField(personInfo, personInfoField.getName());
                }
            }
        }
        if (employeeField.getName().equals("position") || employeeField.getName().equals("experience")) {
            employeeService.updateField(employee, employeeField.getName());
        }
        PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() +
                " FIELD " + employeeField.getName() + " WAS UPDATED FROM " + oldValue + " TO " + newValue);
    }

    public static void removePackage() {
        PRINT2LN.info("REMOVING PACKAGE");
        PackageService packageService = new PackageServiceDaoImpl();
        List<Package> packages = packageService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the package:");
        for (Package pack : packages) {
            printAsMenu.print(index, "Package N" + pack.getNumber() + " from " + pack.getAddressFrom().getCountry().getTitle() +
                    " to " + pack.getAddressTo().getCountry().getTitle());
            index++;
        }
        Package pack = packages.get(RequestMethods.getNumberFromChoice("package [number]", index - 1) - 1);
        Long package_id = pack.getId();
        packageService.removeById(package_id);
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS REMOVED");
    }

    public static void updatePackageField() {
        PRINT2LN.info("UPDATING PACKAGE");
        PackageService packageService = new PackageServiceDaoImpl();
        AddressService addressService = new AddressServiceDaoImpl();
        PersonInfoService personInfoService = new PersonInfoServiceDaoImpl();
        PassportService passportService = new PassportServiceDaoImpl();
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        EmployeeService employeeService = new EmployeeServiceDaoImpl();
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
                pack.setPackageType((PackageType) getEnumFromConsole(PackageType.values(), "package type"));
                newValue = pack.getPackageType().getTitle();
            }
            case ("deliveryType") -> {
                oldValue = pack.getDeliveryType().getTitle();
                pack.setDeliveryType((DeliveryType) getEnumFromConsole(DeliveryType.values(), "delivery type"));
                newValue = pack.getDeliveryType().getTitle();
            }
            case ("status") -> {
                oldValue = pack.getStatus().getTitle();
                pack.setStatus((Status) getEnumFromConsole(Status.values(), packageField.getName()));
                newValue = pack.getStatus().getTitle();
            }
            case ("condition") -> {
                oldValue = pack.getCondition().getTitle();
                pack.setCondition((Condition) getEnumFromConsole(Condition.values(), packageField.getName()));
                newValue = pack.getCondition().getTitle();
            }
            case ("addressFrom") -> {
                Address addressFrom = pack.getAddressFrom();
                Field addressField = getAnyClassFieldFromConsole(Address.class);
                List<String> values = switchAddressField(addressField, addressFrom);
                oldValue = values.get(0);
                newValue = values.get(1);
                addressService.updateField(addressFrom, addressField.getName());
            }
            case ("addressTo") -> {
                Address addressTo = pack.getAddressTo();
                Field addressField = getAnyClassFieldFromConsole(Address.class);
                List<String> values = switchAddressField(addressField, addressTo);
                oldValue = values.get(0);
                newValue = values.get(1);
                addressService.updateField(addressTo, addressField.getName());
            }
            case ("customer") -> {
                PersonInfo personInfo = pack.getCustomer().getPersonInfo();
                Field personInfoField = getAnyClassFieldFromConsole(PersonInfo.class);
                switch (personInfoField.getName()) {
                    case ("firstName") -> {
                        oldValue = personInfo.getFirstName();
                        personInfo.setFirstName(RequestMethods.getStringValueFromConsole("first name"));
                        newValue = personInfo.getFirstName();
                    }
                    case ("lastName") -> {
                        oldValue = personInfo.getLastName();
                        personInfo.setLastName(RequestMethods.getStringValueFromConsole("last name"));
                        newValue = personInfo.getLastName();
                    }
                    case ("age") -> {
                        oldValue = String.valueOf(personInfo.getAge());
                        personInfo.setAge(RequestMethods.getIntegerValueFromConsole(personInfoField.getName()));
                        newValue = String.valueOf(personInfo.getAge());
                    }
                    case ("passport") -> {
                        Passport passport = pack.getCustomer().getPersonInfo().getPassport();
                        oldValue = passport.getNumber();
                        passport.setNumber(RequestMethods.getStringValueFromConsole("passport number"));
                        newValue = passport.getNumber();
                        passportService.updateField(passport);
                    }
                    case ("address") -> {
                        Address customerAddress = pack.getCustomer().getPersonInfo().getAddress();
                        Field addressField = getAnyClassFieldFromConsole(Address.class);
                        List<String> values = switchAddressField(addressField, customerAddress);
                        oldValue = values.get(0);
                        newValue = values.get(1);
                        addressService.updateField(customerAddress, addressField.getName());
                    }
                }
                if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                    personInfoService.updateField(personInfo, personInfoField.getName());
                }
            }
            case ("employee") -> {
                Employee employee = pack.getEmployee();
                Field employeeField = getEmployeeClassFieldFromConsole();
                switch (employeeField.getName()) {
                    case ("position") -> {
                        oldValue = employee.getPosition().getTitle();
                        employee.setPosition((Position) getEnumFromConsole(Position.values(), employeeField.getName()));
                        newValue = employee.getPosition().getTitle();
                    }
                    case ("experience") -> {
                        oldValue = employee.getExperience().getTitle();
                        employee.setExperience((Experience) getEnumFromConsole(Experience.values(), employeeField.getName()));
                        newValue = employee.getExperience().getTitle();
                    }
                    case ("department") -> {
                        Department department = employee.getDepartment();
                        oldValue = department.getTitle();
                        department.setTitle(RequestMethods.getStringValueFromConsole(employeeField.getName()));
                        newValue = department.getTitle();
                        departmentService.rename(department);
                    }
                    case ("personInfo") -> {
                        PersonInfo personInfo = pack.getEmployee().getPersonInfo();
                        Field personInfoField = getAnyClassFieldFromConsole(PersonInfo.class);
                        switch (personInfoField.getName()) {
                            case ("firstName") -> {
                                oldValue = personInfo.getFirstName();
                                personInfo.setFirstName(RequestMethods.getStringValueFromConsole("first name"));
                                newValue = personInfo.getFirstName();
                            }
                            case ("lastName") -> {
                                oldValue = personInfo.getLastName();
                                personInfo.setLastName(RequestMethods.getStringValueFromConsole("last name"));
                                newValue = personInfo.getLastName();
                            }
                            case ("age") -> {
                                oldValue = String.valueOf(personInfo.getAge());
                                personInfo.setAge(RequestMethods.getIntegerValueFromConsole(personInfoField.getName()));
                                newValue = String.valueOf(personInfo.getAge());
                            }
                            case ("passport") -> {
                                Passport passport = pack.getCustomer().getPersonInfo().getPassport();
                                oldValue = passport.getNumber();
                                passport.setNumber(RequestMethods.getStringValueFromConsole("passport number"));
                                newValue = passport.getNumber();
                                passportService.updateField(passport);
                            }
                            case ("address") -> {
                                Address employeeAddress = pack.getCustomer().getPersonInfo().getAddress();
                                Field addressField = getAnyClassFieldFromConsole(Address.class);
                                List<String> values = switchAddressField(addressField, employeeAddress);
                                oldValue = values.get(0);
                                newValue = values.get(1);
                                addressService.updateField(employeeAddress, addressField.getName());
                            }
                        }
                        if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
                            personInfoService.updateField(personInfo, personInfoField.getName());
                        }
                    }
                }
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
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " FIELD " + packageField.getName() + " WAS UPDATED FROM " +
                oldValue + " TO " + newValue);
    }

    private static Department getExistingDepartment() {
        DepartmentService departmentService = new DepartmentServiceDaoImpl();
        List<Department> departments = departmentService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the department:");
        for (Department item : departments) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        return departments.get(RequestMethods.getNumberFromChoice("department number", index - 1) - 1);
    }

    private static Employee getExistingEmployee() {
        EmployeeService employeeService = new EmployeeServiceDaoImpl();
        List<Employee> employees = employeeService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the employee:");
        for (Employee employee : employees) {
            printAsMenu.print(index, employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName());
            index++;
        }
        return employees.get(RequestMethods.getNumberFromChoice("employee number", index - 1) - 1);
    }

    private static Customer getExistingCustomer() {
        CustomerService customerService = new CustomerServiceDaoImpl();
        List<Customer> customers = customerService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the customer:");
        for (Customer customer : customers) {
            printAsMenu.print(index, customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName());
            index++;
        }
        return customers.get(RequestMethods.getNumberFromChoice("employee number", index - 1) - 1);
    }

    private static Package getExistingPackage() {
        PackageService packageService = new PackageServiceDaoImpl();
        List<Package> packages = packageService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the package:");
        for (Package pack : packages) {
            printAsMenu.print(index, "Package N" + pack.getNumber() + " from " + pack.getAddressFrom().getCountry().getTitle() +
                    " to " + pack.getAddressTo().getCountry().getTitle());
            index++;
        }
        return packages.get(RequestMethods.getNumberFromChoice("package [number]", index - 1) - 1);
    }

    private static Field getAnyClassFieldFromConsole(Class<?> clazz) {
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

    private static Field getEmployeeClassFieldFromConsole() {
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

    private static List<String> switchAddressField(Field addressField, Address address) {
        List<String> values = new ArrayList<>();
        switch (addressField.getName()) {
            case ("city") -> {
                values.add(address.getCity());
                address.setCity(RequestMethods.getStringValueFromConsole(addressField.getName()));
                values.add(address.getCity());
            }
            case ("street") -> {
                values.add(address.getStreet());
                address.setStreet(RequestMethods.getStringValueFromConsole(addressField.getName()));
                values.add(address.getStreet());
            }
            case ("house") -> {
                values.add(String.valueOf(address.getHouse()));
                address.setHouse(RequestMethods.getIntegerValueFromConsole(addressField.getName()));
                values.add(String.valueOf(address.getHouse()));
            }
            case ("flat") -> {
                values.add(String.valueOf(address.getFlat()));
                address.setFlat(RequestMethods.getIntegerValueFromConsole(addressField.getName()));
                values.add(String.valueOf(address.getFlat()));
            }
            case ("zipCode") -> {
                values.add(String.valueOf(address.getZipCode()));
                address.setZipCode(RequestMethods.getIntegerValueFromConsole("zip code"));
                values.add(String.valueOf(address.getZipCode()));
            }
            case ("country") -> {
                values.add(address.getCountry().getTitle());
                address.setCountry((Country) getEnumFromConsole(Country.values(), addressField.getName()));
                values.add(address.getCountry().getTitle());
            }
        }
        return values;
    }
}
