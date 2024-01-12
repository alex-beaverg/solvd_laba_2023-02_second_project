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
import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.*;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.NegativeNumberException;
import com.solvd.delivery_service.util.custom_exceptions.YearsOfExperienceException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.solvd.delivery_service.util.Printers.*;

public class AdminActions extends Actions {

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

    public static void showDepartments() {
        PRINT2LN.info("ALL DEPARTMENTS:");
        for (Department department : new DepartmentServiceImpl().retrieveAll()) {
            PRINTLN.info("DEPARTMENT: " + department + "\n\tEMPLOYEES:");
            department.getEmployees().forEach(employee -> PRINTLN.info("\t- " + employee + ", Salary:[" +
                    Accounting.calculateEmployeeSalary(employee) + " BYN]"));
        }
    }

    public static void showCompanies() {
        PRINT2LN.info("ALL COMPANIES:");
        for (Company company : new CompanyServiceImpl().retrieveAll()) {
            PRINTLN.info(company + "\n\tDEPARTMENTS:");
            company.getDepartments().forEach(department -> PRINTLN.info("\t- " + department));
        }
    }

    public static void showEmployees() {
        PRINT2LN.info("ALL EMPLOYEES:");
        for (Employee employee : new EmployeeServiceImpl().retrieveAll()) {
            PRINTLN.info("EMPLOYEE: " + employee + ", Salary:[" +
                    Accounting.calculateEmployeeSalary(employee) + " BYN]\n\tPACKAGES:");
            employee.getPackages().forEach(pack -> PRINTLN.info("\t- " + pack + ", Cost:[" +
                    Accounting.calculatePackageCost(pack) + " BYN]"));
        }
    }

    public static void showPackages() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceImpl().retrieveAll()) {
            PRINTLN.info(pack + ", Cost:[" + Accounting.calculatePackageCost(pack) + " BYN]");
        }
    }

    public static void showCustomers() {
        PRINT2LN.info("ALL CUSTOMERS:");
        for (Customer customer : new CustomerServiceImpl().retrieveAll()) {
            PRINTLN.info("CUSTOMER: " + customer + "\n\tPACKAGES:");
            customer.getPackages().forEach(pack -> PRINTLN.info("\t- " + pack + ", Cost:[" +
                    Accounting.calculatePackageCost(pack) + " BYN]"));
        }
    }

    public static void registerEmployee() {
        PRINT2LN.info("REGISTERING EMPLOYEE");
        Passport employeePassport = getPassportFromConsole();
        Address employeeAddress = getAddressFromConsole();
        PersonInfo employeePersonInfo = getPersonInfoFromConsole(employeePassport, employeeAddress);
        Position employeePosition = (Position) getEnumValueFromConsole(Position.values(), "position");
        Experience employeeExperience = getExperienceDependingOnYears();
        Department employeeDepartment = getExistingDepartment();
        Employee employee = new Employee(employeePosition, employeeExperience, employeeDepartment, employeePersonInfo);
        EmployeeService employeeService = new EmployeeServiceImpl();
        employeeService.create(employee, employeeDepartment.getId());
        PRINT2LN.info("EMPLOYEE " + employeePersonInfo.getFirstName() + " " + employeePersonInfo.getLastName() + " WAS REGISTERED");
        PRINTLN.info("EMPLOYEE SALARY: " + Accounting.calculateEmployeeSalary(employee) + " BYN");
    }

    public static void registerDepartment() {
        PRINT2LN.info("REGISTERING DEPARTMENT");
        Company company = getExistingCompany();
        Department department = new Department(RequestMethods.getStringValueFromConsole("department title"), company);
        DepartmentService departmentService = new DepartmentServiceImpl();
        departmentService.create(department);
        PRINT2LN.info("DEPARTMENT " + department.getTitle() + " WAS REGISTERED");
    }

    public static void registerCompany() {
        PRINT2LN.info("REGISTERING COMPANY");
        Company company = new Company(RequestMethods.getStringValueFromConsole("company name"));
        CompanyService companyService = new CompanyServiceImpl();
        companyService.create(company);
        PRINT2LN.info("COMPANY " + company.getName() + " WAS REGISTERED");
    }

    public static void removeDepartment() {
        PRINT2LN.info("REMOVING DEPARTMENT");
        DepartmentService departmentService = new DepartmentServiceImpl();
        Department department = getExistingDepartment();
        Long department_id = department.getId();
        departmentService.removeById(department_id);
        PRINT2LN.info("DEPARTMENT " + department.getTitle() + " WAS REMOVED");
    }

    public static void removeCompany() {
        PRINT2LN.info("REMOVING COMPANY");
        CompanyService companyService = new CompanyServiceImpl();
        Company company = getExistingCompany();
        Long company_id = company.getId();
        companyService.removeById(company_id);
        PRINT2LN.info("COMPANY " + company.getName() + " WAS REMOVED");
    }

    public static void updateDepartmentField() {
        PRINT2LN.info("UPDATING DEPARTMENT");
        DepartmentService departmentService = new DepartmentServiceImpl();
        CompanyService companyService = new CompanyServiceImpl();
        Department department = getExistingDepartment();
        Field departmentField = getDepartmentClassFieldFromConsole();
        String oldValue = "", newValue = "";
        switch (departmentField.getName()) {
            case ("title") -> {
                oldValue = department.getTitle();
                department.setTitle(RequestMethods.getStringValueFromConsole(departmentField.getName()));
                newValue = department.getTitle();
            }
            case ("company") -> {
                Company company = department.getCompany();
                oldValue = company.getName();
                company.setName(RequestMethods.getStringValueFromConsole("company name"));
                newValue = company.getName();
                companyService.updateField(company);
            }
        }
        if (departmentField.getName().equals("title")) {
            departmentService.updateField(department);
        }
        PRINT2LN.info("DEPARTMENT " + department.getTitle() + " FIELD " + departmentField.getName() +
                " WAS UPDATED FROM " + oldValue + " TO " + newValue);
    }

    public static void renameCompany() {
        PRINT2LN.info("RENAMING COMPANY");
        CompanyService companyService = new CompanyServiceImpl();
        Company company = getExistingCompany();
        String newCompanyName = RequestMethods.getStringValueFromConsole("new company name");
        String oldCompanyName = company.getName();
        company.setName(newCompanyName);
        companyService.updateField(company);
        PRINT2LN.info("COMPANY " + oldCompanyName + " WAS RENAMED TO " + newCompanyName);
    }

    public static void removeCustomer() {
        PRINT2LN.info("REMOVING CUSTOMER");
        CustomerService customerService = new CustomerServiceImpl();
        Customer customer = getExistingCustomer();
        Long customer_id = customer.getId();
        customerService.removeById(customer_id);
        PRINT2LN.info("CUSTOMER " + customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName() + " WAS REMOVED");
    }

    public static void updateCustomerField() {
        PRINT2LN.info("UPDATING CUSTOMER");
        PersonInfoService personInfoService = new PersonInfoServiceImpl();
        Customer customer = getExistingCustomer();
        String oldValue = "", newValue = "";
        PersonInfo personInfo = customer.getPersonInfo();
        Field personInfoField = getAnyClassFieldFromConsole(PersonInfo.class);
        Map<String, String> values = switchPersonInfoField(personInfoField, personInfo);
        oldValue = values.get("old");
        newValue = values.get("new");
        if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
            personInfoService.updateField(personInfo, personInfoField.getName());
        }
        PRINT2LN.info("CUSTOMER " + customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName() +
                " FIELD " + personInfoField.getName() + " WAS UPDATED FROM " + oldValue + " TO " + newValue);
    }

    public static void removeEmployee() {
        PRINT2LN.info("REMOVING EMPLOYEE");
        EmployeeService employeeService = new EmployeeServiceImpl();
        Employee employee = getExistingEmployee();
        Long employee_id = employee.getId();
        employeeService.removeById(employee_id);
        PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() + " WAS REMOVED");
    }

    public static void updateEmployeeField() {
        PRINT2LN.info("UPDATING EMPLOYEE");
        EmployeeService employeeService = new EmployeeServiceImpl();
        Employee employee = getExistingEmployee();
        Field employeeField = getEmployeeClassFieldFromConsole();
        String oldValue = "", newValue = "";
        Map<String, String> values = switchEmployeeField(employeeField, employee);
        oldValue = values.get("old");
        newValue = values.get("new");
        if (employeeField.getName().equals("position") || employeeField.getName().equals("experience")) {
            employeeService.updateField(employee, employeeField.getName());
        }
        PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() +
                " FIELD " + employeeField.getName() + " WAS UPDATED FROM " + oldValue + " TO " + newValue);
    }

    public static void removePackage() {
        PRINT2LN.info("REMOVING PACKAGE");
        PackageService packageService = new PackageServiceImpl();
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
        PRINT2LN.info("PACKAGE N" + pack.getNumber() + " FIELD " + packageField.getName() + " WAS UPDATED FROM " +
                oldValue + " TO " + newValue);
    }

    private static Department getExistingDepartment() {
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

    private static Company getExistingCompany() {
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

    private static Employee getExistingEmployee() {
        EmployeeService employeeService = new EmployeeServiceImpl();
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
        CustomerService customerService = new CustomerServiceImpl();
        List<Customer> customers = customerService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the customer:");
        for (Customer customer : customers) {
            printAsMenu.print(index, customer.getPersonInfo().getFirstName() + " " + customer.getPersonInfo().getLastName());
            index++;
        }
        return customers.get(RequestMethods.getNumberFromChoice("customer number", index - 1) - 1);
    }

    private static Package getExistingPackage() {
        PackageService packageService = new PackageServiceImpl();
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

    private static Experience getExperienceDependingOnYears() {
        double years;
        do {
            try {
                years = getYearsFromConsole();
                break;
            } catch (YearsOfExperienceException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        for (Experience experience : Experience.values()) {
            if (years <= experience.getYearsTo()) return experience;
        }
        return null;
    }

    private static double getYearsFromConsole() throws YearsOfExperienceException {
        do {
            try {
                double years = RequestMethods.requestingInfoDouble("Enter experience years: ");
                if (years > 100.0) {
                    throw new YearsOfExperienceException("[YearsException]: Years can not be more than 100!");
                }
                return years;
            } catch (EmptyInputException | NegativeNumberException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
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

    private static Field getDepartmentClassFieldFromConsole() {
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

    private static Map<String, String> switchAddressField(Field addressField, Address address) {
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

    private static Map<String, String> switchPersonInfoField(Field personInfoField, PersonInfo personInfo) {
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

    private static Map<String, String> switchEmployeeField(Field employeeField, Employee employee) {
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
}