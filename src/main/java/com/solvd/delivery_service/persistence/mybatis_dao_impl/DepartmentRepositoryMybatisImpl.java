package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.DepartmentRepository;
import com.solvd.delivery_service.persistence.EmployeeRepository;
import com.solvd.delivery_service.persistence.MybatisConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class DepartmentRepositoryMybatisImpl implements DepartmentRepository {

    @Override
    public void create(Department department) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            departmentRepository.create(department);
        }
    }

    @Override
    public Optional<Department> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            return departmentRepository.findById(id);
        }
    }

    @Override
    public List<Department> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            List<Department> departments = departmentRepository.findAll();
            for (Department department : departments) {
                department.setEmployees(employeeRepository.findDepartmentEmployees(department));
            }
            return departments;
        }
    }

    @Override
    public void update(Department department) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            departmentRepository.update(department);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            departmentRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            return departmentRepository.countOfEntries();
        }
    }

    @Override
    public List<Department> findCompanyDepartments(Company company) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            DepartmentRepository departmentRepository = sqlSession.getMapper(DepartmentRepository.class);
            return departmentRepository.findCompanyDepartments(company);
        }
    }
}