package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.persistence.MybatisConfig;
import com.solvd.delivery_service.persistence.PackageRepository;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class PackageRepositoryMybatisImpl implements PackageRepository {
    @Override
    public void create(Package pack) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            packageRepository.create(pack);
        }
    }

    @Override
    public Optional<Package> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            return packageRepository.findById(id);
        }
    }

    @Override
    public List<Package> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            return packageRepository.findAll();
        }
    }

    @Override
    public void update(Package pack, String field) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            packageRepository.update(pack, field);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            packageRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            return packageRepository.countOfEntries();
        }
    }

    @Override
    public Long findMaxPackageNumber() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            return packageRepository.findMaxPackageNumber();
        }
    }

    @Override
    public List<Package> findCustomerPackages(Customer customer) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            return packageRepository.findCustomerPackages(customer);
        }
    }

    @Override
    public List<Package> findEmployeePackages(Employee employee) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            return packageRepository.findEmployeePackages(employee);
        }
    }
}