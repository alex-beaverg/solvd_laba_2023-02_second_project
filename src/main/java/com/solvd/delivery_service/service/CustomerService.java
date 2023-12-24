package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.pack.Package;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);
    List<Customer> retrieveAll();
    Long retrieveNumberOfEntries();
    List<Customer> retrieveEntriesByLastName(String lastName);
    Customer retrieveById(Long id);
    List<Package> retrieveCustomerPackages(Customer customer);
    void removeById(Long id);
}
