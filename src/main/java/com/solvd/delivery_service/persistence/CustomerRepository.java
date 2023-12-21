package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.customer.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void create(Customer customer);
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    void update(Customer customer);
    void deleteById(Long id);
}
