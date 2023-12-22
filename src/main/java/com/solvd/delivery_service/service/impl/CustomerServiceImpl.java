package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.persistence.CustomerRepository;
import com.solvd.delivery_service.persistence.impl.CustomerRepositoryImpl;
import com.solvd.delivery_service.service.CustomerService;
import com.solvd.delivery_service.service.PersonInfoService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PersonInfoService personInfoService;

    public CustomerServiceImpl() {
        this.customerRepository = new CustomerRepositoryImpl();
        this.personInfoService = new PersonInfoServiceImpl();
    }

    @Override
    public Customer create(Customer customer) {
        customer.setId(null);
        if (customer.getPersonInfo() != null) {
            PersonInfo personInfo = personInfoService.create(customer.getPersonInfo());
            customer.setPersonInfo(personInfo);
        }
        customerRepository.create(customer);
        return customer;
    }

    @Override
    public List<Customer> retrieveAll() {
        return customerRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return customerRepository.countOfEntries();
    }

    @Override
    public List<Customer> retrieveEntriesByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    @Override
    public Customer retrieveById(Long id) {
        return customerRepository.findById(id).get();
    }
}
