package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;

import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    Department retrieveById(Long id);
    List<Department> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Department department);
    void removeById(Long id);
    List<Department> retrieveCompanyDepartments(Company company);
}