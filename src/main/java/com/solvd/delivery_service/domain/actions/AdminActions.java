package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.human.employee.Experience;
import com.solvd.delivery_service.domain.human.employee.Position;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.DepartmentService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.MenuItemOutOfBoundsException;
import com.solvd.delivery_service.util.custom_exceptions.StringFormatException;

import java.util.List;

import static com.solvd.delivery_service.util.Printers.*;

public class AdminActions extends Actions {

    public static void showNumberOfEntriesInDb() {
        PRINT2LN.info("NUMBER OF TABLE ENTRIES IN DATABASE:");
        PRINTLN.info("Addresses table: " + new AddressServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Customers table: " + new CustomerServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Departments table: " + new DepartmentServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Employees table: " + new EmployeeServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Packages table: " + new PackageServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Passports table: " + new PassportServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Persons table: " + new PersonInfoServiceImpl().retrieveNumberOfEntries() + " entries");
    }

    public static void showDepartments() {
        PRINT2LN.info("ALL DEPARTMENTS:");
        for (Department department : new DepartmentServiceImpl().retrieveAll()) {
            PRINTLN.info("id:[" +
                    department.getId() + "], Title:[" +
                    department.getTitle() + "]");
        }
    }

    public static void showEmployees() {
        PRINT2LN.info("ALL EMPLOYEES:");
        for (Employee employee : new EmployeeServiceImpl().retrieveAll()) {
            PRINTLN.info("id:[" +
                    employee.getId() + "], Name:[" +
                    employee.getPersonInfo().getFirstName() + " " +
                    employee.getPersonInfo().getLastName() + "], Department:[" +
                    employee.getDepartment().getTitle() + "], Position:[" +
                    employee.getPosition().getTitle() + "], Experience:[" +
                    employee.getExperience().getTitle() + "], Age:[" +
                    employee.getPersonInfo().getAge() + "], Passport:[" +
                    employee.getPersonInfo().getPassport().getNumber() + "], Address:[" +
                    employee.getPersonInfo().getAddress().getZipCode() + ", " +
                    employee.getPersonInfo().getAddress().getCountry().getTitle() + ", " +
                    employee.getPersonInfo().getAddress().getCity() + ", " +
                    employee.getPersonInfo().getAddress().getStreet() + ", " +
                    employee.getPersonInfo().getAddress().getHouse() + "-" +
                    employee.getPersonInfo().getAddress().getFlat() + "], Salary:[" +
                    Accounting.calculateEmployeeSalary(employee) + " BYN]");
        }
    }

    public static void showPackages() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceImpl().retrieveAll()) {
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

    public static void registerEmployee() {
        PRINT2LN.info("REGISTERING EMPLOYEE");
        Passport employeePassport = setPassport();
        Address employeeAddress = setAddress();
        PersonInfo employeePersonInfo = setPersonInfo(employeePassport, employeeAddress);
        Position employeePosition = setPosition();
        Experience employeeExperience = setExperience();
        Department employeeDepartment = setExistDepartment();
        Employee employee = new Employee(employeePosition, employeeExperience, employeeDepartment, employeePersonInfo);
        EmployeeService employeeService = new EmployeeServiceImpl();
        employeeService.createWithExistDepartment(employee);
        PRINT2LN.info("EMPLOYEE " + employeePersonInfo.getFirstName() + " " + employeePersonInfo.getLastName() + " WAS REGISTERED");
        PRINT2LN.info("EMPLOYEE SALARY: " + Accounting.calculateEmployeeSalary(employee) + " BYN");
    }

    public static void registerDepartment() {
        PRINT2LN.info("REGISTERING DEPARTMENT");
        Department department = setDepartment();
        DepartmentService departmentService = new DepartmentServiceImpl();
        departmentService.create(department);
        PRINT2LN.info("DEPARTMENT " + department.getTitle() + " WAS REGISTERED");
    }

    private static Position setPosition() {
        int index = 1;
        PRINTLN.info("Choose the position:");
        for (Position item : Position.values()) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter number of position: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return Position.values()[answer - 1];
    }

    private static Experience setExperience() {
        int index = 1;
        PRINTLN.info("Choose the experience:");
        for (Experience item : Experience.values()) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter number of experience: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return Experience.values()[answer - 1];
    }

    private static Department setExistDepartment() {
        DepartmentService departmentService = new DepartmentServiceImpl();
        List<Department> departments = departmentService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the department:");
        for (Department item : departments) {
            printAsMenu.print(index, item.getTitle());
            index++;
        }
        int answer;
        do {
            try {
                answer = RequestMethods.requestingInfoWithChoice("Enter number of department: ", index - 1);
                break;
            } catch (EmptyInputException | MenuItemOutOfBoundsException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("[NumberFormatException]: Entered data is not a number!");
            }
        } while (true);
        return departments.get(answer - 1);
    }

    protected static Department setDepartment() {
        String departmentTitle;
        do {
            try {
                departmentTitle = RequestMethods.requestingInfoString("Enter department title: ");
                break;
            } catch (EmptyInputException | StringFormatException e) {
                LOGGER.error(e.getMessage());
            }
        } while (true);
        return new Department(departmentTitle);
    }
}
