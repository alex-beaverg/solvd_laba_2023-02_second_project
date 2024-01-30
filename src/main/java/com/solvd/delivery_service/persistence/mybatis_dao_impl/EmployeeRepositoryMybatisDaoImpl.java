package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.EmployeeRepository;
import com.solvd.delivery_service.persistence.MybatisConfig;
import com.solvd.delivery_service.persistence.PackageRepository;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryMybatisDaoImpl implements EmployeeRepository {
    @Override
    public void create(Employee employee, Long departmentId) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            employeeRepository.create(employee, departmentId);
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            return employeeRepository.findById(id);
        }
    }

    @Override
    public List<Employee> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            PackageRepository packageRepository = sqlSession.getMapper(PackageRepository.class);
            List<Employee> employees = employeeRepository.findAll();
            for (Employee employee : employees) {
                employee.setPackages(packageRepository.findEmployeePackages(employee));
            }
            return employees;
        }
    }

    @Override
    public void update(Employee employee, String field) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            employeeRepository.update(employee, field);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            employeeRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            return employeeRepository.countOfEntries();
        }
    }

    @Override
    public List<Employee> findDepartmentEmployees(Department department) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            EmployeeRepository employeeRepository = sqlSession.getMapper(EmployeeRepository.class);
            return employeeRepository.findDepartmentEmployees(department);
        }
    }
}