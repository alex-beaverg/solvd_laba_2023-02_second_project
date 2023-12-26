package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.customer.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);
    Customer retrieveById(Long id);
    List<Customer> retrieveAll();
    Long retrieveNumberOfEntries();
    void removeById(Long id);
    List<Customer> retrieveEntriesByLastName(String lastName);
}