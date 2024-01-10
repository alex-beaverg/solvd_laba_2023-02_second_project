package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.persistence.*;
import com.solvd.delivery_service.persistence.basic_dao_impl.*;
import com.solvd.delivery_service.persistence.mybatis_dao_impl.*;

import static com.solvd.delivery_service.util.Printers.*;

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

    protected void assignBasicDaoService() {
        PRINT2LN.info("RUNNING USING 'BASIC DAO' SERVICE");
        isBasicDaoService = true;
    }

    protected void assignMybatisDaoService() {
        PRINT2LN.info("RUNNING USING 'MYBATIS DAO' SERVICE");
        isBasicDaoService = false;
    }

    public <T> T getRepository(Class<T> clazz) {
        if (clazz.equals(AddressRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new AddressRepositoryDaoImpl());
            }
            return clazz.cast(new AddressRepositoryMybatisImpl());
        } else if (clazz.equals(CustomerRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new CustomerRepositoryDaoImpl());
            }
            return clazz.cast(new CustomerRepositoryMybatisImpl());
        } else if (clazz.equals(DepartmentRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new DepartmentRepositoryDaoImpl());
            }
            return clazz.cast(new DepartmentRepositoryMybatisImpl());
        } else if (clazz.equals(EmployeeRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new EmployeeRepositoryDaoImpl());
            }
            return clazz.cast(new EmployeeRepositoryMybatisImpl());
        } else if (clazz.equals(PackageRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new PackageRepositoryDaoImpl());
            }
            return clazz.cast(new PackageRepositoryMybatisImpl());
        } else if (clazz.equals(PassportRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new PassportRepositoryDaoImpl());
            }
            return clazz.cast(new PassportRepositoryMybatisImpl());
        } else if (clazz.equals(PersonInfoRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new PersonInfoRepositoryDaoImpl());
            }
            return clazz.cast(new PersonInfoRepositoryMybatisImpl());
        } else if (clazz.equals(CompanyRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new CompanyRepositoryDaoImpl());
            }
            return clazz.cast(new CompanyRepositoryMybatisImpl());
        }
        return null;
    }
}