package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.customer.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void create(@Param("customer") Customer customer);
    Optional<Customer> findById(@Param("id") Long id);
    List<Customer> findAll();
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
    List<Customer> findByLastName(@Param("lastName") String lastName);
}