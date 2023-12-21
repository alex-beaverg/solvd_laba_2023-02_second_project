package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.customer.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);
    List<Customer> retrieveAll();
}
