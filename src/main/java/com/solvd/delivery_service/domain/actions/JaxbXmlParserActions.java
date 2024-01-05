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
import com.solvd.delivery_service.util.DateAdapter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

import static com.solvd.delivery_service.util.Printers.*;

public class JaxbXmlParserActions implements IParserActions {
    @Override
    public void createPackageWithRegistrationNewCustomerFromFile() {
        File fileWithCustomer = new File("src/main/resources/xml_data/new_customer.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Customer customer = (Customer) unmarshaller.unmarshal(fileWithCustomer);
            customer.setId(null);
            Employee employee = UserActions.getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
            Package pack = UserActions.registerPackage(customer, employee);
            PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
            PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPackageFromFile() {
        PackageService packageService = new PackageServiceImpl();
        File fileWithPackage = new File("src/main/resources/xml_data/new_package.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Package.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Package pack = (Package) unmarshaller.unmarshal(fileWithPackage);
            pack.setId(null);
            pack.getCustomer().setId(null);
            pack.getEmployee().setId(null);
            Package packToCreate = packageService.createWithNewEmployee(pack);
            PRINT2LN.info("PACKAGE N" + packToCreate.getNumber() + " WAS CREATED");
            PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(packToCreate) + " BYN");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerEmployeeFromFile() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        File fileWithEmployee = new File("src/main/resources/xml_data/new_employee.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Employee.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Employee employee = (Employee) unmarshaller.unmarshal(fileWithEmployee);
            employeeService.create(employee, employee.getDepartment().getId());
            PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() + " WAS REGISTERED");
            PRINTLN.info("EMPLOYEE SALARY: " + Accounting.calculateEmployeeSalary(employee) + " BYN");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerCompanyFromFile() {
        CompanyService companyService = new CompanyServiceImpl();
        DepartmentService departmentService = new DepartmentServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();
        File fileWithCompany = new File("src/main/resources/xml_data/new_company.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Company.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Company company = (Company) unmarshaller.unmarshal(fileWithCompany);
            company = companyService.create(company);
            for (int i = 0; i < company.getDepartments().size(); i++) {
                Department department = company.getDepartments().get(i);
                department.setCompany(company);
                department = departmentService.create(department);
                for (Employee employee : department.getEmployees()) {
                    employeeService.create(employee, department.getId());
                }
            }
            PRINT2LN.info("COMPANY " + company.getName() + " WAS REGISTERED");
            PRINTLN.info("DATE FROM XML-FILE: " + new DateAdapter().marshal(company.getDate()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
