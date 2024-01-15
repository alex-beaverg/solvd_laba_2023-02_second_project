package com.solvd.delivery_service.domain.actions.entity;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.CompanyService;
import com.solvd.delivery_service.service.DepartmentService;
import com.solvd.delivery_service.service.impl.CompanyServiceImpl;
import com.solvd.delivery_service.service.impl.DepartmentServiceImpl;
import com.solvd.delivery_service.util.console_menu.RequestMethods;

import java.lang.reflect.Field;

import static com.solvd.delivery_service.util.Printers.*;

public class DepartmentActions extends Actions implements IEntityActions {

    @Override
    public void showEntityEntries() {
        PRINT2LN.info("ALL DEPARTMENTS:");
        for (Department department : new DepartmentServiceImpl().retrieveAll()) {
            PRINTLN.info(String.format("DEPARTMENT: %s\n\tEMPLOYEES:", department));
            department.getEmployees()
                    .forEach(employee -> PRINTLN.info(String.format("\t- %s, Salary:[%s BYN]",
                            employee, accounting.calculateEmployeeSalary(employee))));
        }
    }

    @Override
    public void registerEntityEntry() {
        PRINT2LN.info("REGISTERING DEPARTMENT");
        Company company = getExistingCompany();
        Department department = new Department(RequestMethods.getStringValueFromConsole("department title"), company);
        DepartmentService departmentService = new DepartmentServiceImpl();
        departmentService.create(department);
        PRINT2LN.info(String.format("DEPARTMENT %s WAS REGISTERED", department.getTitle()));
    }

    @Override
    public void removeEntityEntry() {
        PRINT2LN.info("REMOVING DEPARTMENT");
        DepartmentService departmentService = new DepartmentServiceImpl();
        Department department = getExistingDepartment();
        Long department_id = department.getId();
        departmentService.removeById(department_id);
        PRINT2LN.info(String.format("DEPARTMENT %s WAS REMOVED", department.getTitle()));
    }

    @Override
    public void updateEntityEntryField() {
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
        PRINT2LN.info(String.format("DEPARTMENT %s FIELD %s WAS UPDATED FROM %s TO %s",
                department.getTitle(), departmentField.getName(), oldValue, newValue));
    }
}