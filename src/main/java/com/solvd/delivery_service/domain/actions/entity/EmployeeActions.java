package com.solvd.delivery_service.domain.actions.entity;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.human.employee.Experience;
import com.solvd.delivery_service.domain.human.employee.Position;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.impl.EmployeeServiceImpl;
import com.solvd.delivery_service.util.console_menu.RequestMethods;
import com.solvd.delivery_service.util.custom_exceptions.EmptyInputException;
import com.solvd.delivery_service.util.custom_exceptions.NegativeNumberException;
import com.solvd.delivery_service.util.custom_exceptions.YearsOfExperienceException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.solvd.delivery_service.util.Printers.*;

public class EmployeeActions extends Actions implements IEntityActions {

    @Override
    public void showEntityEntries() {
        PRINT2LN.info("ALL EMPLOYEES:");
        for (Employee employee : new EmployeeServiceImpl().retrieveAll()) {
            PRINTLN.info(String.format("EMPLOYEE: %s, Salary:[%s BYN]\n\tPACKAGES:",
                    employee, accounting.calculateEmployeeSalary(employee)));
            employee.getPackages()
                    .forEach(pack -> PRINTLN.info(String.format("\t- %s, Cost:[%s BYN]",
                            pack, accounting.calculatePackageCost(pack))));
        }
    }

    @Override
    public void registerEntityEntry() {
        PRINT2LN.info("REGISTERING EMPLOYEE");
        Passport employeePassport = new Passport(RequestMethods.getStringValueFromConsole("passport number"));
        Address employeeAddress = getAddressFromConsole();
        PersonInfo employeePersonInfo = getPersonInfoFromConsole(employeePassport, employeeAddress);
        Position employeePosition = (Position) getEnumValueFromConsole(Position.values(), "position");
        Experience employeeExperience = getExperienceDependingOnYears();
        Department employeeDepartment = getExistingDepartment();
        Employee employee = new Employee(employeePosition, employeeExperience, employeeDepartment, employeePersonInfo);
        EmployeeService employeeService = new EmployeeServiceImpl();
        employeeService.create(employee, employeeDepartment.getId());
        String firstName = employeePersonInfo.getFirstName();
        String lastName = employeePersonInfo.getLastName();
        PRINT2LN.info(String.format("EMPLOYEE %s %s WAS REGISTERED", firstName, lastName));
        PRINTLN.info(String.format("EMPLOYEE SALARY: %s BYN", accounting.calculateEmployeeSalary(employee)));
    }

    @Override
    public void removeEntityEntry() {
        PRINT2LN.info("REMOVING EMPLOYEE");
        EmployeeService employeeService = new EmployeeServiceImpl();
        Employee employee = getExistingEmployee();
        Long employee_id = employee.getId();
        employeeService.removeById(employee_id);
        String firstName = employee.getPersonInfo().getFirstName();
        String lastName = employee.getPersonInfo().getLastName();
        PRINT2LN.info(String.format("EMPLOYEE %s %s WAS REMOVED", firstName, lastName));
    }

    @Override
    public void updateEntityEntryField() {
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
        String firstName = employee.getPersonInfo().getFirstName();
        String lastName = employee.getPersonInfo().getLastName();
        PRINT2LN.info(String.format("EMPLOYEE %s %s FIELD %s WAS UPDATED FROM %s TO %s",
                firstName, lastName, employeeField.getName(), oldValue, newValue));
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

    private static Employee getExistingEmployee() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        List<Employee> employees = employeeService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the employee:");
        for (Employee employee : employees) {
            printAsMenu.print(index, String.format("%s %s", employee.getPersonInfo().getFirstName(), employee.getPersonInfo().getLastName()));
            index++;
        }
        return employees.get(RequestMethods.getNumberFromChoice("employee number", index - 1) - 1);
    }
}
