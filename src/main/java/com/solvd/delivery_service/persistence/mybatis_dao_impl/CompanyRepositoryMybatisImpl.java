package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.persistence.CompanyRepository;
import com.solvd.delivery_service.persistence.DepartmentRepository;
import com.solvd.delivery_service.persistence.MybatisConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class CompanyRepositoryMybatisImpl implements CompanyRepository {
    @Override
    public void create(Company company) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CompanyRepository companyRepository = sqlSession.getMapper(CompanyRepository.class);
            companyRepository.create(company);
        }
    }

    @Override
    public Optional<Company> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CompanyRepository companyRepository = sqlSession.getMapper(CompanyRepository.class);
            return companyRepository.findById(id);
        }
    }

    @Override
    public List<Company> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CompanyRepository companyRepository = sqlSession.getMapper(CompanyRepository.class);
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            List<Company> companies = companyRepository.findAll();
            for (Company company : companies) {
                company.setDepartments(departmentRepository.findCompanyDepartments(company));
            }
            return companies;
        }
    }

    @Override
    public void update(Company company) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CompanyRepository companyRepository = sqlSession.getMapper(CompanyRepository.class);
            companyRepository.update(company);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CompanyRepository companyRepository = sqlSession.getMapper(CompanyRepository.class);
            companyRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            CompanyRepository companyRepository = sqlSession.getMapper(CompanyRepository.class);
            return companyRepository.countOfEntries();
        }
    }
}
