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
import com.solvd.delivery_service.util.XmlDateAdapter;
import com.solvd.delivery_service.util.XmlSchemaValidator;
import com.solvd.delivery_service.util.custom_exceptions.XsdValidateException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

import static com.solvd.delivery_service.util.Printers.*;

public class JaxbXmlParserActions extends UserActions implements IParserActions {
    @Override
    public void createPackageWithRegistrationNewCustomerFromFile() {
        File xmlFileWithCustomer = new File("src/main/resources/xml_data/new_customer.xml");
        File xsdFileWithCustomer = new File("src/main/resources/xml_data/new_customer.xsd");
        try {
            XmlSchemaValidator.validate(xmlFileWithCustomer, xsdFileWithCustomer);
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Customer customer = (Customer) unmarshaller.unmarshal(xmlFileWithCustomer);
            customer.setId(getCustomerIdByPassport(customer.getPersonInfo().getPassport()));
            Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
            Package pack = registerPackage(customer, employee);
            PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
            PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
            PRINTLN.info("CUSTOMER WAS TAKEN FROM XML FILE: '" + xmlFileWithCustomer.getName() + "'");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (XsdValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("PACKAGE WAS NOT CREATED");
        }
    }

    @Override
    public void createPackageFromFile() {
        PackageService packageService = new PackageServiceImpl();
        File xmlFileWithPackage = new File("src/main/resources/xml_data/new_package.xml");
        File xsdFileWithPackage = new File("src/main/resources/xml_data/new_package.xsd");
        try {
            XmlSchemaValidator.validate(xmlFileWithPackage, xsdFileWithPackage);
            JAXBContext context = JAXBContext.newInstance(Package.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Package pack = (Package) unmarshaller.unmarshal(xmlFileWithPackage);
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
            PRINT2LN.info("PACKAGE N" + packToCreate.getNumber() + " WAS CREATED");
            PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(packToCreate) + " BYN");
            PRINTLN.info("PACKAGE WAS TAKEN FROM XML FILE: '" + xmlFileWithPackage.getName() + "'");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (XsdValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("PACKAGE WAS NOT CREATED");
        }
    }

    @Override
    public void registerEmployeeFromFile() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        File xmlFileWithEmployee = new File("src/main/resources/xml_data/new_employee.xml");
        File xsdFileWithEmployee = new File("src/main/resources/xml_data/new_employee.xsd");
        try {
            XmlSchemaValidator.validate(xmlFileWithEmployee, xsdFileWithEmployee);
            JAXBContext context = JAXBContext.newInstance(Employee.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Employee employee = (Employee) unmarshaller.unmarshal(xmlFileWithEmployee);
            if (getEmployeeIdByPassport(employee.getPersonInfo().getPassport()) != null) {
                PRINT2LN.info("EXISTING EMPLOYEE WAS NOT RE-REGISTERED");
            } else {
                employeeService.create(employee, employee.getDepartment().getId());
                PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() + " WAS REGISTERED");
                PRINTLN.info("EMPLOYEE SALARY: " + Accounting.calculateEmployeeSalary(employee) + " BYN");
                PRINTLN.info("EMPLOYEE WAS TAKEN FROM XML FILE: '" + xmlFileWithEmployee.getName() + "'");
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (XsdValidateException e) {
            LOGGER.error(e.getMessage());
            PRINT2LN.info("EMPLOYEE WAS NOT REGISTERED");
        }
    }

    @Override
    public void registerCompanyFromFile() {
        CompanyService companyService = new CompanyServiceImpl();
        DepartmentService departmentService = new DepartmentServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();
        File xmlFileWithCompany = new File("src/main/resources/xml_data/new_company.xml");
        File xsdFileWithCompany = new File("src/main/resources/xml_data/new_company.xsd");
        try {
            XmlSchemaValidator.validate(xmlFileWithCompany, xsdFileWithCompany);
            JAXBContext context = JAXBContext.newInstance(Company.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Company company = (Company) unmarshaller.unmarshal(xmlFileWithCompany);
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
                PRINT2LN.info("COMPANY " + company.getName() + " WAS REGISTERED");
                PRINTLN.info("DATE FROM XML FILE: " + new XmlDateAdapter().marshal(company.getDate()));
                PRINTLN.info("COMPANY WAS TAKEN FROM XML FILE: '" + xmlFileWithCompany.getName() + "'");
            } else {
                PRINT2LN.info("EXISTING COMPANY WAS NOT RE-REGISTERED");
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (XsdValidateException e) {
            LOGGER.error(e.getMessage());
            PRINTLN.info("COMPANY WAS NOT REGISTERED");
        }
    }
}