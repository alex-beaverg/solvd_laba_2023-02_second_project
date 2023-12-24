package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.DepartmentRepository;
import com.solvd.delivery_service.persistence.impl.DepartmentRepositoryDaoImpl;
import com.solvd.delivery_service.service.DepartmentService;

import java.util.List;

public class DepartmentServiceDaoImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceDaoImpl() {
        this.departmentRepository = new DepartmentRepositoryDaoImpl();
    }

    @Override
    public Department create(Department department) {
        department.setId(null);
        departmentRepository.create(department);
        return department;
    }

    @Override
    public List<Department> retrieveAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return departmentRepository.countOfEntries();
    }

    @Override
    public void removeById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public void rename(Department department) {
        departmentRepository.update(department);
    }

    @Override
    public List<Employee> retrieveDepartmentEmployees(Department department) {
        return departmentRepository.findDepartmentEmployees(department);
    }

    @Override
    public Department retrieveById(Long id) {
        return departmentRepository.findById(id).get();
    }
}
