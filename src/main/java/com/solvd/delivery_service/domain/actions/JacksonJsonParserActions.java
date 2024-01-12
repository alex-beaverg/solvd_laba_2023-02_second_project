package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.service.CompanyService;
import com.solvd.delivery_service.service.DepartmentService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PackageService;
import com.solvd.delivery_service.service.impl.CompanyServiceImpl;
import com.solvd.delivery_service.service.impl.DepartmentServiceImpl;
import com.solvd.delivery_service.service.impl.EmployeeServiceImpl;
import com.solvd.delivery_service.service.impl.PackageServiceImpl;
import com.solvd.delivery_service.util.JsonDateAdapter;
import com.solvd.delivery_service.util.JsonReader;
import com.solvd.delivery_service.util.custom_exceptions.JsonValidateException;

import java.io.File;

import static com.solvd.delivery_service.util.Printers.*;

public class JacksonJsonParserActions extends UserActions implements IParserActions {
    @Override
    public void createPackageWithRegistrationNewCustomerFromFile() {
        File jsonFileWithCustomer = new File("src/main/resources/json_data/new_customer.json");
        try {
            Customer customer = JsonReader.validateAndReadValue(jsonFileWithCustomer, Customer.class);
            customer.setId(getCustomerIdByPassport(customer.getPersonInfo().getPassport()));
            Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
            Package pack = registerPackage(customer, employee);
            PRINT2LN.info(String.format("PACKAGE N%d WAS CREATED", pack.getNumber()));
            PRINTLN.info(String.format("PACKAGE COST: %s BYN", Accounting.calculatePackageCost(pack)));
            PRINTLN.info(String.format("CUSTOMER WAS TAKEN FROM JSON FILE: '%s'", jsonFileWithCustomer.getName()));
        } catch (JsonValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("PACKAGE WAS NOT CREATED");
        }
    }

    @Override
    public void createPackageFromFile() {
        PackageService packageService = new PackageServiceImpl();
        File jsonFileWithPackage = new File("src/main/resources/json_data/new_package.json");
        try {
            Package pack = JsonReader.validateAndReadValue(jsonFileWithPackage, Package.class);
            pack.setId(null);
            pack.setNumber(getPackageNumberByPackage(pack));
            pack.getCustomer().setId(getCustomerIdByPassport(pack.getCustomer().getPersonInfo().getPassport()));
            pack.getEmployee().setId(getEmployeeIdByPassport(pack.getEmployee().getPersonInfo().getPassport()));
            Package packToCreate;
            if (pack.getCustomer().getId() == null && pack.getEmployee().getId() == null) {
                packToCreate = packageService.createWithNewEmployeeAndNewCustomer(pack);
            } else if (pack.getCustomer().getId() != null && pack.getEmployee().getId() == null) {
                packToCreate = packageService.createWithExistingCustomerAndNewEmployee(pack);
            } else if (pack.getCustomer().getId() == null && pack.getEmployee().getId() != null) {
                packToCreate = packageService.createWithExistingEmployeeAndNewCustomer(pack);
            } else {
                packToCreate = packageService.createWithExistingCustomerAndEmployee(pack);
            }
            PRINT2LN.info(String.format("PACKAGE N%d WAS CREATED", packToCreate.getNumber()));
            PRINTLN.info(String.format("PACKAGE COST: %s BYN", Accounting.calculatePackageCost(packToCreate)));
            PRINTLN.info(String.format("PACKAGE WAS TAKEN FROM JSON FILE: '%s'", jsonFileWithPackage.getName()));
        } catch (JsonValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("PACKAGE WAS NOT CREATED");
        }
    }

    @Override
    public void registerEmployeeFromFile() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        File jsonFileWithEmployee = new File("src/main/resources/json_data/new_employee.json");
        try {
            Employee employee = JsonReader.validateAndReadValue(jsonFileWithEmployee, Employee.class);
            if (getEmployeeIdByPassport(employee.getPersonInfo().getPassport()) != null) {
                PRINT2LN.info("EXISTING EMPLOYEE WAS NOT RE-REGISTERED");
            } else {
                employeeService.create(employee, employee.getDepartment().getId());
                String firstName = employee.getPersonInfo().getFirstName();
                String lastName = employee.getPersonInfo().getLastName();
                PRINT2LN.info(String.format("EMPLOYEE %s %s WAS REGISTERED", firstName, lastName));
                PRINTLN.info(String.format("EMPLOYEE SALARY: %s BYN", Accounting.calculateEmployeeSalary(employee)));
                PRINTLN.info(String.format("EMPLOYEE WAS TAKEN FROM JSON FILE: '%s'", jsonFileWithEmployee.getName()));
            }
        } catch (JsonValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("EMPLOYEE WAS NOT REGISTERED");
        }
    }

    @Override
    public void registerCompanyFromFile() {
        CompanyService companyService = new CompanyServiceImpl();
        DepartmentService departmentService = new DepartmentServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();
        File jsonFileWithCompany = new File("src/main/resources/json_data/new_company.json");
        try {
            Company company = JsonReader.validateAndReadValue(jsonFileWithCompany, Company.class);
            if (!isCompanyExist(company)) {
                company = companyService.create(company);
                for (int i = 0; i < company.getDepartments().size(); i++) {
                    Department department = company.getDepartments().get(i);
                    department.setCompany(company);
                    department = departmentService.create(department);
                    for (Employee employee : department.getEmployees()) {
                        employeeService.create(employee, department.getId());
                    }
                }
                PRINT2LN.info(String.format("COMPANY %s WAS REGISTERED", company.getName()));
                PRINTLN.info(String.format("DATE FROM JSON FILE: %s", new JsonDateAdapter().serialize(company.getDate())));
                PRINTLN.info(String.format("COMPANY WAS TAKEN FROM JSON FILE: '%s'", jsonFileWithCompany.getName()));
            } else {
                PRINT2LN.info("EXISTING COMPANY WAS NOT RE-REGISTERED");
            }
        } catch (JsonValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("COMPANY WAS NOT REGISTERED");
        }
    }
}