package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.pack.Package;

import java.util.List;

public interface PackageService {
    Package create(Package pack);
    Package retrieveById(Long id);
    List<Package> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Package pack, String field);
    void removeById(Long id);
    Package createWithExistingCustomer(Package pack);
    Long retrieveMaxPackageNumber();
    List<Package> retrieveCustomerPackages(Customer customer);
    Package createWithNewEmployee(Package pack);
    Package createWithExistingAddressTo(Package pack);
}