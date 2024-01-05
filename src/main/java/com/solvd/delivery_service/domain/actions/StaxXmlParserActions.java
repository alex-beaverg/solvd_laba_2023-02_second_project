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
import com.solvd.delivery_service.util.XmlDateAdapter;
import com.solvd.delivery_service.util.XmlSchemaValidator;
import com.solvd.delivery_service.util.custom_exceptions.XsdException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.solvd.delivery_service.util.Printers.*;

public class StaxXmlParserActions extends UserActions implements IParserActions {
    @Override
    public void createPackageWithRegistrationNewCustomerFromFile() {
        File xmlFileWithCustomer = new File("src/main/resources/xml_data/new_customer.xml");
        File xsdFileWithCustomer = new File("src/main/resources/xml_data/new_customer.xsd");
        try {
            XmlSchemaValidator.validate(xmlFileWithCustomer, xsdFileWithCustomer);
            Customer customer = parseCustomerFromXmlUsingStax(xmlFileWithCustomer);
            Employee employee = getRandomEmployeeFromDataBase(new DepartmentServiceImpl().retrieveById(1L));
            Package pack = registerPackage(customer, employee);
            PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
            PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
        } catch (XsdException e) {
            LOGGER.error(e.getMessage());
            PRINTLN.info("PACKAGE WAS NOT CREATED");
        }
    }

    @Override
    public void createPackageFromFile() {
        AddressService addressService = new AddressServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();
        Package pack = new Package();
        Address addressFrom = new Address();
        Address addressTo = new Address();
        File xmlFileWithPackage = new File("src/main/resources/xml_data/new_package.xml");
        File xsdFileWithPackage = new File("src/main/resources/xml_data/new_package.xsd");
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try (FileInputStream fis = new FileInputStream(xmlFileWithPackage)) {
            XmlSchemaValidator.validate(xmlFileWithPackage, xsdFileWithPackage);
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case ("packageNumber") -> {
                            nextEvent = reader.nextEvent();
                            pack.setNumber(Long.parseLong(nextEvent.asCharacters().getData()));
                        }
                        case ("packageType") -> {
                            nextEvent = reader.nextEvent();
                            pack.setPackageType(PackageType.valueOf(nextEvent.asCharacters().getData()));
                        }
                        case ("deliveryType") -> {
                            nextEvent = reader.nextEvent();
                            pack.setDeliveryType(DeliveryType.valueOf(nextEvent.asCharacters().getData()));
                        }
                        case ("status") -> {
                            nextEvent = reader.nextEvent();
                            pack.setStatus(Status.valueOf(nextEvent.asCharacters().getData()));
                        }
                        case ("condition") -> {
                            nextEvent = reader.nextEvent();
                            pack.setCondition(Condition.valueOf(nextEvent.asCharacters().getData()));
                        }
                        case ("addressFrom") -> {
                            while (reader.hasNext()) {
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isStartElement()) {
                                    startElement = nextEvent.asStartElement();
                                    switch (startElement.getName().getLocalPart()) {
                                        case ("city") -> {
                                            nextEvent = reader.nextEvent();
                                            addressFrom.setCity(nextEvent.asCharacters().getData());
                                        }
                                        case ("street") -> {
                                            nextEvent = reader.nextEvent();
                                            addressFrom.setStreet(nextEvent.asCharacters().getData());
                                        }
                                        case ("house") -> {
                                            nextEvent = reader.nextEvent();
                                            addressFrom.setHouse(Integer.parseInt(nextEvent.asCharacters().getData()));
                                        }
                                        case ("flat") -> {
                                            nextEvent = reader.nextEvent();
                                            addressFrom.setFlat(Integer.parseInt(nextEvent.asCharacters().getData()));
                                        }
                                        case ("zipCode") -> {
                                            nextEvent = reader.nextEvent();
                                            addressFrom.setZipCode(Integer.parseInt(nextEvent.asCharacters().getData()));
                                        }
                                        case ("country") -> {
                                            nextEvent = reader.nextEvent();
                                            addressFrom.setCountry(Country.valueOf(nextEvent.asCharacters().getData()));
                                        }
                                    }
                                }
                                if (nextEvent.isEndElement()) {
                                    EndElement endElement = nextEvent.asEndElement();
                                    if (endElement.getName().getLocalPart().equals("addressFrom")) {
                                        pack.setAddressFrom(addressService.create(addressFrom));
                                        break;
                                    }
                                }
                            }
                        }
                        case ("addressTo") -> {
                            while (reader.hasNext()) {
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isStartElement()) {
                                    startElement = nextEvent.asStartElement();
                                    switch (startElement.getName().getLocalPart()) {
                                        case ("city") -> {
                                            nextEvent = reader.nextEvent();
                                            addressTo.setCity(nextEvent.asCharacters().getData());
                                        }
                                        case ("street") -> {
                                            nextEvent = reader.nextEvent();
                                            addressTo.setStreet(nextEvent.asCharacters().getData());
                                        }
                                        case ("house") -> {
                                            nextEvent = reader.nextEvent();
                                            addressTo.setHouse(Integer.parseInt(nextEvent.asCharacters().getData()));
                                        }
                                        case ("flat") -> {
                                            nextEvent = reader.nextEvent();
                                            addressTo.setFlat(Integer.parseInt(nextEvent.asCharacters().getData()));
                                        }
                                        case ("zipCode") -> {
                                            nextEvent = reader.nextEvent();
                                            addressTo.setZipCode(Integer.parseInt(nextEvent.asCharacters().getData()));
                                        }
                                        case ("country") -> {
                                            nextEvent = reader.nextEvent();
                                            addressTo.setCountry(Country.valueOf(nextEvent.asCharacters().getData()));
                                            pack.setAddressTo(addressService.create(addressTo));
                                        }
                                    }
                                }
                                if (nextEvent.isEndElement()) {
                                    EndElement endElement = nextEvent.asEndElement();
                                    if (endElement.getName().getLocalPart().equals("addressTo")) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Employee employee = parseEmployeeFromXmlUsingStax(xmlFileWithPackage);
            pack.setEmployee(employeeService
                    .create(employee, new DepartmentServiceImpl().retrieveAll()
                            .stream()
                            .filter(department -> department.getTitle().equals(employee.getDepartment().getTitle()))
                            .findFirst().get().getId()));
            pack.setCustomer(parseCustomerFromXmlUsingStax(xmlFileWithPackage));
            PackageService packageService = new PackageServiceImpl();
            packageService.createWithExistingAddressTo(pack);
            PRINT2LN.info("PACKAGE N" + pack.getNumber() + " WAS CREATED");
            PRINTLN.info("PACKAGE COST: " + Accounting.calculatePackageCost(pack) + " BYN");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (XsdException e) {
            LOGGER.error(e.getMessage());
            PRINTLN.info("PACKAGE WAS NOT CREATED");
        }
    }

    @Override
    public void registerEmployeeFromFile() {
        File xmlFileWithEmployee = new File("src/main/resources/xml_data/new_employee.xml");
        File xsdFileWithEmployee = new File("src/main/resources/xml_data/new_employee.xsd");
        try {
            XmlSchemaValidator.validate(xmlFileWithEmployee, xsdFileWithEmployee);
            EmployeeService employeeService = new EmployeeServiceImpl();
            Employee employee = parseEmployeeFromXmlUsingStax(xmlFileWithEmployee);
            employeeService.create(employee, employee.getDepartment().getId());
            PRINT2LN.info("EMPLOYEE " + employee.getPersonInfo().getFirstName() + " " + employee.getPersonInfo().getLastName() + " WAS REGISTERED");
            PRINTLN.info("EMPLOYEE SALARY: " + Accounting.calculateEmployeeSalary(employee) + " BYN");
        } catch (XsdException e) {
            LOGGER.error(e.getMessage());
            PRINTLN.info("EMPLOYEE WAS NOT REGISTERED");
        }
    }

    @Override
    public void registerCompanyFromFile() {
        Company company = new Company();
        CompanyService companyService = new CompanyServiceImpl();
        Department department = new Department();
        DepartmentService departmentService = new DepartmentServiceImpl();
        Employee employee = new Employee();
        EmployeeService employeeService = new EmployeeServiceImpl();
        PersonInfo personInfo = new PersonInfo();
        Passport passport = new Passport();
        Address address = new Address();
        File xmlFileWithCompany = new File("src/main/resources/xml_data/new_company.xml");
        File xsdFileWithCompany = new File("src/main/resources/xml_data/new_company.xsd");
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try (FileInputStream fis = new FileInputStream(xmlFileWithCompany)) {
            XmlSchemaValidator.validate(xmlFileWithCompany, xsdFileWithCompany);
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case ("name") -> {
                            nextEvent = reader.nextEvent();
                            company.setName(nextEvent.asCharacters().getData());
                            company = companyService.create(company);
                        }
                        case ("department") -> {
                            while (reader.hasNext()) {
                                nextEvent = reader.nextEvent();
                                if (nextEvent.isStartElement()) {
                                    startElement = nextEvent.asStartElement();
                                    switch (startElement.getName().getLocalPart()) {
                                        case ("title") -> {
                                            nextEvent = reader.nextEvent();
                                            department.setTitle(nextEvent.asCharacters().getData());
                                            department.setCompany(company);
                                            department = departmentService.create(department);
                                        }
                                        case ("employee") -> {
                                            while (reader.hasNext()) {
                                                nextEvent = reader.nextEvent();
                                                if (nextEvent.isStartElement()) {
                                                    startElement = nextEvent.asStartElement();
                                                    switch (startElement.getName().getLocalPart()) {
                                                        case ("position") -> {
                                                            nextEvent = reader.nextEvent();
                                                            employee.setPosition(Position.valueOf(nextEvent.asCharacters().getData()));
                                                        }
                                                        case ("experience") -> {
                                                            nextEvent = reader.nextEvent();
                                                            employee.setExperience(Experience.valueOf(nextEvent.asCharacters().getData()));
                                                            employee.setDepartment(department);
                                                        }
                                                        case ("personInfo") -> {
                                                            while (reader.hasNext()) {
                                                                nextEvent = reader.nextEvent();
                                                                if (nextEvent.isStartElement()) {
                                                                    startElement = nextEvent.asStartElement();
                                                                    switch (startElement.getName().getLocalPart()) {
                                                                        case ("firstName") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            personInfo.setFirstName(nextEvent.asCharacters().getData());
                                                                        }
                                                                        case ("lastName") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            personInfo.setLastName(nextEvent.asCharacters().getData());
                                                                        }
                                                                        case ("age") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            personInfo.setAge(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                        }
                                                                        case ("number") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            passport.setNumber(nextEvent.asCharacters().getData());
                                                                            personInfo.setPassport(passport);
                                                                        }
                                                                        case ("city") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            address.setCity(nextEvent.asCharacters().getData());
                                                                        }
                                                                        case ("street") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            address.setStreet(nextEvent.asCharacters().getData());
                                                                        }
                                                                        case ("house") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            address.setHouse(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                        }
                                                                        case ("flat") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            address.setFlat(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                        }
                                                                        case ("zipCode") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            address.setZipCode(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                        }
                                                                        case ("country") -> {
                                                                            nextEvent = reader.nextEvent();
                                                                            address.setCountry(Country.valueOf(nextEvent.asCharacters().getData()));
                                                                            personInfo.setAddress(address);
                                                                        }
                                                                    }
                                                                }
                                                                if (nextEvent.isEndElement()) {
                                                                    EndElement endElement = nextEvent.asEndElement();
                                                                    if (endElement.getName().getLocalPart().equals("personInfo")) {
                                                                        employee.setPersonInfo(personInfo);
                                                                        employeeService.create(employee, employee.getDepartment().getId());
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (nextEvent.isEndElement()) {
                                                    EndElement endElement = nextEvent.asEndElement();
                                                    if (endElement.getName().getLocalPart().equals("employee")) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (nextEvent.isEndElement()) {
                                    EndElement endElement = nextEvent.asEndElement();
                                    if (endElement.getName().getLocalPart().equals("department")) {
                                        break;
                                    }
                                }
                            }
                        }
                        case ("date") -> {
                            nextEvent = reader.nextEvent();
                            company.setDate(new XmlDateAdapter().unmarshal(nextEvent.asCharacters().getData()));
                        }
                    }
                }
            }
            PRINT2LN.info("COMPANY " + company.getName() + " WAS REGISTERED");
            PRINTLN.info("DATE FROM XML-FILE: " + new XmlDateAdapter().marshal(company.getDate()));
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (XsdException e) {
            LOGGER.error(e.getMessage());
            PRINTLN.info("COMPANY WAS NOT REGISTERED");
        }
    }

    private static Customer parseCustomerFromXmlUsingStax(File file) {
        Customer customer = new Customer();
        PersonInfo customerPersonInfo = new PersonInfo();
        Address customerAddress = new Address();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try (FileInputStream fis = new FileInputStream(file)) {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("customer")) {
                        while (reader.hasNext()) {
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isStartElement()) {
                                startElement = nextEvent.asStartElement();
                                if (startElement.getName().getLocalPart().equals("personInfo")) {
                                    while (reader.hasNext()) {
                                        nextEvent = reader.nextEvent();
                                        if (nextEvent.isStartElement()) {
                                            startElement = nextEvent.asStartElement();
                                            switch (startElement.getName().getLocalPart()) {
                                                case ("firstName") -> {
                                                    nextEvent = reader.nextEvent();
                                                    customerPersonInfo.setFirstName(nextEvent.asCharacters().getData());
                                                }
                                                case ("lastName") -> {
                                                    nextEvent = reader.nextEvent();
                                                    customerPersonInfo.setLastName(nextEvent.asCharacters().getData());
                                                }
                                                case ("age") -> {
                                                    nextEvent = reader.nextEvent();
                                                    customerPersonInfo.setAge(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                }
                                                case ("address") -> {
                                                    while (reader.hasNext()) {
                                                        nextEvent = reader.nextEvent();
                                                        if (nextEvent.isStartElement()) {
                                                            startElement = nextEvent.asStartElement();
                                                            switch (startElement.getName().getLocalPart()) {
                                                                case ("city") -> {
                                                                    nextEvent = reader.nextEvent();
                                                                    customerAddress.setCity(nextEvent.asCharacters().getData());
                                                                }
                                                                case ("street") -> {
                                                                    nextEvent = reader.nextEvent();
                                                                    customerAddress.setStreet(nextEvent.asCharacters().getData());
                                                                }
                                                                case ("house") -> {
                                                                    nextEvent = reader.nextEvent();
                                                                    customerAddress.setHouse(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                }
                                                                case ("flat") -> {
                                                                    nextEvent = reader.nextEvent();
                                                                    customerAddress.setFlat(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                }
                                                                case ("zipCode") -> {
                                                                    nextEvent = reader.nextEvent();
                                                                    customerAddress.setZipCode(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                }
                                                                case ("country") -> {
                                                                    nextEvent = reader.nextEvent();
                                                                    customerAddress.setCountry(Country.valueOf(nextEvent.asCharacters().getData()));
                                                                    customerPersonInfo.setAddress(customerAddress);
                                                                    customer.setPersonInfo(customerPersonInfo);
                                                                }
                                                            }
                                                        }
                                                        if (nextEvent.isEndElement()) {
                                                            EndElement endElement = nextEvent.asEndElement();
                                                            if (endElement.getName().getLocalPart().equals("address")) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (nextEvent.isEndElement()) {
                                            EndElement endElement = nextEvent.asEndElement();
                                            if (endElement.getName().getLocalPart().equals("personInfo")) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (nextEvent.isEndElement()) {
                                EndElement endElement = nextEvent.asEndElement();
                                if (endElement.getName().getLocalPart().equals("customer")) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
        customerPersonInfo.setPassport(parsePassportFromXmlUsingStax(file, "customer"));
        return customer;
    }

    private static Employee parseEmployeeFromXmlUsingStax(File file) {
        Employee employee = new Employee();
        PersonInfo employeePersonInfo = new PersonInfo();
        Address employeeAddress = new Address();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try (FileInputStream fis = new FileInputStream(file)) {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("employee")) {
                        while (reader.hasNext()) {
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isStartElement()) {
                                startElement = nextEvent.asStartElement();
                                switch (startElement.getName().getLocalPart()) {
                                    case ("position") -> {
                                        nextEvent = reader.nextEvent();
                                        employee.setPosition(Position.valueOf(nextEvent.asCharacters().getData()));
                                    }
                                    case ("experience") -> {
                                        nextEvent = reader.nextEvent();
                                        employee.setExperience(Experience.valueOf(nextEvent.asCharacters().getData()));
                                    }
                                    case ("department") -> {
                                        while (reader.hasNext()) {
                                            nextEvent = reader.nextEvent();
                                            if (nextEvent.isStartElement()) {
                                                startElement = nextEvent.asStartElement();
                                                if (startElement.getName().getLocalPart().equals("title")) {
                                                    nextEvent = reader.nextEvent();
                                                    String next = nextEvent.asCharacters().getData();
                                                    employee.setDepartment(new DepartmentServiceImpl().retrieveAll().stream()
                                                            .filter(department -> department.getTitle().equals(next))
                                                            .findFirst().get());
                                                } else if (startElement.getName().getLocalPart().equals("company")) {
                                                    while (reader.hasNext()) {
                                                        nextEvent = reader.nextEvent();
                                                        if (nextEvent.isStartElement()) {
                                                            startElement = nextEvent.asStartElement();
                                                            if (startElement.getName().getLocalPart().equals("name")) {
                                                                nextEvent = reader.nextEvent();
                                                                String next = nextEvent.asCharacters().getData();
                                                                employee.getDepartment().setCompany(new CompanyServiceImpl().retrieveAll().stream()
                                                                        .filter(company -> company.getName().equals(next))
                                                                        .findFirst().get());
                                                            }
                                                        }
                                                        if (nextEvent.isEndElement()) {
                                                            EndElement endElement = nextEvent.asEndElement();
                                                            if (endElement.getName().getLocalPart().equals("company")) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (nextEvent.isEndElement()) {
                                                EndElement endElement = nextEvent.asEndElement();
                                                if (endElement.getName().getLocalPart().equals("department")) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    case ("personInfo") -> {
                                        while (reader.hasNext()) {
                                            nextEvent = reader.nextEvent();
                                            if (nextEvent.isStartElement()) {
                                                startElement = nextEvent.asStartElement();
                                                switch (startElement.getName().getLocalPart()) {
                                                    case ("firstName") -> {
                                                        nextEvent = reader.nextEvent();
                                                        employeePersonInfo.setFirstName(nextEvent.asCharacters().getData());
                                                    }
                                                    case ("lastName") -> {
                                                        nextEvent = reader.nextEvent();
                                                        employeePersonInfo.setLastName(nextEvent.asCharacters().getData());
                                                    }
                                                    case ("age") -> {
                                                        nextEvent = reader.nextEvent();
                                                        employeePersonInfo.setAge(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                    }
                                                    case ("address") -> {
                                                        while (reader.hasNext()) {
                                                            nextEvent = reader.nextEvent();
                                                            if (nextEvent.isStartElement()) {
                                                                startElement = nextEvent.asStartElement();
                                                                switch (startElement.getName().getLocalPart()) {
                                                                    case ("city") -> {
                                                                        nextEvent = reader.nextEvent();
                                                                        employeeAddress.setCity(nextEvent.asCharacters().getData());
                                                                    }
                                                                    case ("street") -> {
                                                                        nextEvent = reader.nextEvent();
                                                                        employeeAddress.setStreet(nextEvent.asCharacters().getData());
                                                                    }
                                                                    case ("house") -> {
                                                                        nextEvent = reader.nextEvent();
                                                                        employeeAddress.setHouse(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                    }
                                                                    case ("flat") -> {
                                                                        nextEvent = reader.nextEvent();
                                                                        employeeAddress.setFlat(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                    }
                                                                    case ("zipCode") -> {
                                                                        nextEvent = reader.nextEvent();
                                                                        employeeAddress.setZipCode(Integer.parseInt(nextEvent.asCharacters().getData()));
                                                                    }
                                                                    case ("country") -> {
                                                                        nextEvent = reader.nextEvent();
                                                                        employeeAddress.setCountry(Country.valueOf(nextEvent.asCharacters().getData()));
                                                                        employeePersonInfo.setAddress(employeeAddress);
                                                                        employee.setPersonInfo(employeePersonInfo);
                                                                    }
                                                                }
                                                            }
                                                            if (nextEvent.isEndElement()) {
                                                                EndElement endElement = nextEvent.asEndElement();
                                                                if (endElement.getName().getLocalPart().equals("address")) {
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (nextEvent.isEndElement()) {
                                                EndElement endElement = nextEvent.asEndElement();
                                                if (endElement.getName().getLocalPart().equals("personInfo")) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (nextEvent.isEndElement()) {
                                EndElement endElement = nextEvent.asEndElement();
                                if (endElement.getName().getLocalPart().equals("employee")) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
        employeePersonInfo.setPassport(parsePassportFromXmlUsingStax(file, "employee"));
        return employee;
    }

    private static Passport parsePassportFromXmlUsingStax(File file, String personType) {
        Passport passport = new Passport();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try (FileInputStream fis = new FileInputStream(file)) {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals(personType)) {
                        while (reader.hasNext()) {
                            nextEvent = reader.nextEvent();
                            if (nextEvent.isStartElement()) {
                                startElement = nextEvent.asStartElement();
                                if (startElement.getName().getLocalPart().equals("number")) {
                                    nextEvent = reader.nextEvent();
                                    passport.setNumber(nextEvent.asCharacters().getData());
                                }
                            }
                            if (nextEvent.isEndElement()) {
                                EndElement endElement = nextEvent.asEndElement();
                                if (endElement.getName().getLocalPart().equals(personType)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return passport;
    }
}