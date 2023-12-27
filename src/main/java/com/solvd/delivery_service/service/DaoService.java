package com.solvd.delivery_service.service;

import com.solvd.delivery_service.persistence.*;
import com.solvd.delivery_service.persistence.impl.*;

public class DaoService {
    private static DaoService instance;
    private boolean isBasicDaoService;

    private DaoService() {}

    public static DaoService getInstance() {
        if (instance == null) {
            instance = new DaoService();
            instance.isBasicDaoService = true;
        }
        return instance;
    }

    public void assignBasicDaoService() {
        isBasicDaoService = true;
    }

    public void assignMybatisDaoService() {
        isBasicDaoService = false;
    }

    public AddressRepository getAddressRepository() {
        if (isBasicDaoService) {
            return new AddressRepositoryDaoImpl();
        }
        return new AddressRepositoryMybatisImpl();
    }

    public CustomerRepository getCustomerRepository() {
        if (isBasicDaoService) {
            return new CustomerRepositoryDaoImpl();
        }
        return new CustomerRepositoryMybatisImpl();
    }

    public DepartmentRepository getDepartmentRepository() {
        if (isBasicDaoService) {
            return new DepartmentRepositoryDaoImpl();
        }
        return new DepartmentRepositoryMybatisImpl();
    }

    public EmployeeRepository getEmployeeRepository() {
        if (isBasicDaoService) {
            return new EmployeeRepositoryDaoImpl();
        }
        return new EmployeeRepositoryMyBatisImpl();
    }

    public PackageRepository getPackageRepository() {
        if (isBasicDaoService) {
            return new PackageRepositoryDaoImpl();
        }
        return new PackageRepositoryMybatisImpl();
    }

    public PassportRepository getPassportRepository() {
        if (isBasicDaoService) {
            return new PassportRepositoryDaoImpl();
        }
        return new PassportRepositoryMybatisImpl();
    }

    public PersonInfoRepository getPersonInfoRepository() {
        if (isBasicDaoService) {
            return new PersonInfoRepositoryDaoImpl();
        }
        return new PersonInfoRepositoryMybatisImpl();
    }
}