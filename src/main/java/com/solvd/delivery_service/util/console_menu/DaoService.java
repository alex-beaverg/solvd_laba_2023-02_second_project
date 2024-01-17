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
                return clazz.cast(new AddressRepositoryBasicDaoImpl());
            }
            return clazz.cast(new AddressRepositoryMybatisDaoImpl());
        } else if (clazz.equals(CustomerRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new CustomerRepositoryBasicDaoImpl());
            }
            return clazz.cast(new CustomerRepositoryMybatisDaoImpl());
        } else if (clazz.equals(DepartmentRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new DepartmentRepositoryBasicDaoImpl());
            }
            return clazz.cast(new DepartmentRepositoryMybatisDaoImpl());
        } else if (clazz.equals(EmployeeRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new EmployeeRepositoryBasicDaoImpl());
            }
            return clazz.cast(new EmployeeRepositoryMybatisDaoImpl());
        } else if (clazz.equals(PackageRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new PackageRepositoryBasicDaoImpl());
            }
            return clazz.cast(new PackageRepositoryMybatisDaoImpl());
        } else if (clazz.equals(PassportRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new PassportRepositoryBasicDaoImpl());
            }
            return clazz.cast(new PassportRepositoryMybatisDaoImpl());
        } else if (clazz.equals(PersonInfoRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new PersonInfoRepositoryBasicDaoImpl());
            }
            return clazz.cast(new PersonInfoRepositoryMybatisDaoImpl());
        } else if (clazz.equals(CompanyRepository.class)) {
            if (isBasicDaoService) {
                return clazz.cast(new CompanyRepositoryBasicDaoImpl());
            }
            return clazz.cast(new CompanyRepositoryMybatisDaoImpl());
        }
        return null;
    }
}