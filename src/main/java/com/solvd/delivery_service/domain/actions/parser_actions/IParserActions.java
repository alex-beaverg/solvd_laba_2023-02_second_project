package com.solvd.delivery_service.domain.actions.parser_actions;

import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.service.impl.CompanyServiceImpl;
import com.solvd.delivery_service.service.impl.CustomerServiceImpl;
import com.solvd.delivery_service.service.impl.EmployeeServiceImpl;
import com.solvd.delivery_service.service.impl.PackageServiceImpl;

public interface IParserActions {

    default Long getEmployeeIdByPassport(Passport passport) {
        for (Employee employee : new EmployeeServiceImpl().retrieveAll()) {
            if (employee.getPersonInfo().getPassport().getNumber().equals(passport.getNumber())) {
                return employee.getId();
            }
        }
        return null;
    }

    default Long getCustomerIdByPassport(Passport passport) {
        for (Customer customer : new CustomerServiceImpl().retrieveAll()) {
            if (customer.getPersonInfo().getPassport().getNumber().equals(passport.getNumber())) {
                return customer.getId();
            }
        }
        return null;
    }

    default Long getPackageNumberByPackage(Package pack) {
        for (Package packFromDb : new PackageServiceImpl().retrieveAll()) {
            if (packFromDb.getNumber().equals(pack.getNumber())) {
                return new PackageServiceImpl().retrieveMaxPackageNumber() + 1;
            }
        }
        return pack.getNumber();
    }

    default boolean isCompanyExist(Company company) {
        boolean result = false;
        for (Company compFromDb : new CompanyServiceImpl().retrieveAll()) {
            if (compFromDb.getName().equals(company.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    void createPackageWithRegistrationNewCustomerFromFile();
    void createPackageFromFile();
    void registerEmployeeFromFile();
    void registerCompanyFromFile();
}