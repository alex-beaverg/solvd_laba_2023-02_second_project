package com.solvd.delivery_service.service;

import com.solvd.delivery_service.persistence.*;
import com.solvd.delivery_service.persistence.impl.*;

public class DBService {
    private static DBService instance;
    private String service;

    private DBService() {}

    public static DBService getInstance() {
        if (instance == null) {
            instance = new DBService();
        }
        return instance;
    }

    public void setService(String serviceName) {
        service = serviceName;
    }

    public AddressRepository getAddressRepository() {
        if (service.equals("DAO")) {
            return new AddressRepositoryDaoImpl();
        }
        return new AddressRepositoryMybatisImpl();
    }

    public CustomerRepository getCustomerRepository() {
        if (service.equals("DAO")) {
            return new CustomerRepositoryDaoImpl();
        }
        return new CustomerRepositoryMybatisImpl();
    }

    public DepartmentRepository getDepartmentRepository() {
        if (service.equals("DAO")) {
            return new DepartmentRepositoryDaoImpl();
        }
        return new DepartmentRepositoryMybatisImpl();
    }

    public EmployeeRepository getEmployeeRepository() {
        if (service.equals("DAO")) {
            return new EmployeeRepositoryDaoImpl();
        }
        return new EmployeeRepositoryMyBatisImpl();
    }

    public PackageRepository getPackageRepository() {
        if (service.equals("DAO")) {
            return new PackageRepositoryDaoImpl();
        }
        return new PackageRepositoryMybatisImpl();
    }

    public PassportRepository getPassportRepository() {
        if (service.equals("DAO")) {
            return new PassportRepositoryDaoImpl();
        }
        return new PassportRepositoryMybatisImpl();
    }

    public PersonInfoRepository getPersonInfoRepository() {
        if (service.equals("DAO")) {
            return new PersonInfoRepositoryDaoImpl();
        }
        return new PersonInfoRepositoryMybatisImpl();
    }
}