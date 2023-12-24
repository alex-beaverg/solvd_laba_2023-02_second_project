package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.persistence.CustomerRepository;
import com.solvd.delivery_service.persistence.impl.CustomerRepositoryDaoImpl;
import com.solvd.delivery_service.service.CustomerService;
import com.solvd.delivery_service.service.PersonInfoService;

import java.util.List;

public class CustomerServiceDaoImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PersonInfoService personInfoService;

    public CustomerServiceDaoImpl() {
        this.customerRepository = new CustomerRepositoryDaoImpl();
        this.personInfoService = new PersonInfoServiceDaoImpl();
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

    @Override
    public List<Package> retrieveCustomerPackages(Customer customer) {
        return customerRepository.findCustomerPackages(customer);
    }

    @Override
    public void removeById(Long id) {
        customerRepository.deleteById(id);
    }
}
