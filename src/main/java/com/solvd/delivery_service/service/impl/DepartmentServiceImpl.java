package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.DepartmentRepository;
import com.solvd.delivery_service.persistence.impl.DepartmentRepositoryImpl;
import com.solvd.delivery_service.service.DepartmentService;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl() {
        this.departmentRepository = new DepartmentRepositoryImpl();
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
}
