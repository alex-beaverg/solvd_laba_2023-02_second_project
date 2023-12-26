package com.solvd.delivery_service.persistence.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.persistence.AddressRepository;
import com.solvd.delivery_service.persistence.MybatisConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class AddressRepositoryMybatisImpl implements AddressRepository {
    @Override
    public void create(Address address) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            addressRepository.create(address);
        }
    }

    @Override
    public Optional<Address> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            return addressRepository.findById(id);
        }
    }

    @Override
    public List<Address> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            return addressRepository.findAll();
        }
    }

    @Override
    public void update(Address address, String field) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            addressRepository.update(address, field);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            addressRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            return addressRepository.countOfEntries();
        }
    }

    @Override
    public Long findMaxId() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            AddressRepository addressRepository = sqlSession.getMapper(AddressRepository.class);
            return addressRepository.findMaxId();
        }
    }
}