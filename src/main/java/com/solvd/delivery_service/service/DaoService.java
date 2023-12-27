package com.solvd.delivery_service.service;

import com.solvd.delivery_service.persistence.*;
import com.solvd.delivery_service.persistence.impl.*;

public class DaoService {
    private static DaoService instance;
    private String daoService;

    private DaoService() {}

    public static DaoService getInstance() {
        if (instance == null) {
            instance = new DaoService();
            instance.daoService = "MYBATIS DAO";
        }
        return instance;
    }

    public void setDaoService(String daoServiceName) {
        daoService = daoServiceName;
    }

    public AddressRepository getAddressRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new AddressRepositoryDaoImpl();
        }
        return new AddressRepositoryMybatisImpl();
    }

    public CustomerRepository getCustomerRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new CustomerRepositoryDaoImpl();
        }
        return new CustomerRepositoryMybatisImpl();
    }

    public DepartmentRepository getDepartmentRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new DepartmentRepositoryDaoImpl();
        }
        return new DepartmentRepositoryMybatisImpl();
    }

    public EmployeeRepository getEmployeeRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new EmployeeRepositoryDaoImpl();
        }
        return new EmployeeRepositoryMyBatisImpl();
    }

    public PackageRepository getPackageRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new PackageRepositoryDaoImpl();
        }
        return new PackageRepositoryMybatisImpl();
    }

    public PassportRepository getPassportRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new PassportRepositoryDaoImpl();
        }
        return new PassportRepositoryMybatisImpl();
    }

    public PersonInfoRepository getPersonInfoRepository() {
        if (daoService.equals("BASIC DAO")) {
            return new PersonInfoRepositoryDaoImpl();
        }
        return new PersonInfoRepositoryMybatisImpl();
    }
}