package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.persistence.CustomerRepository;
import com.solvd.delivery_service.persistence.MybatisConfig;
import com.solvd.delivery_service.persistence.PackageRepository;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class CustomerRepositoryMybatisDaoImpl implements CustomerRepository {
    @Override
    public void create(Customer customer) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CustomerRepository customerRepository = sqlSession.getMapper(CustomerRepository.class);
            customerRepository.create(customer);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CustomerRepository customerRepository = sqlSession.getMapper(CustomerRepository.class);
            return customerRepository.findById(id);
        }
    }

    @Override
    public List<Customer> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CustomerRepository customerRepository = sqlSession.getMapper(CustomerRepository.class);
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            List<Customer> customers = customerRepository.findAll();
            for (Customer customer : customers) {
                customer.setPackages(packageRepository.findCustomerPackages(customer));
            }
            return customers;
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CustomerRepository customerRepository = sqlSession.getMapper(CustomerRepository.class);
            customerRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CustomerRepository customerRepository = sqlSession.getMapper(CustomerRepository.class);
            return customerRepository.countOfEntries();
        }
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CustomerRepository customerRepository = sqlSession.getMapper(CustomerRepository.class);
            return customerRepository.findByLastName(lastName);
        }
    }
}