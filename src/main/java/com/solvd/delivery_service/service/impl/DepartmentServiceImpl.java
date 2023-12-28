package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.DepartmentRepository;
import com.solvd.delivery_service.util.console_menu.DaoService;
import com.solvd.delivery_service.service.DepartmentService;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl() {
        this.departmentRepository = DAO_SERVICE.getRepository(DepartmentRepository.class);
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
    public void updateField(Department department) {
        departmentRepository.update(department);
    }

    @Override
    public Department retrieveById(Long id) {
        return departmentRepository.findById(id).get();
    }
}