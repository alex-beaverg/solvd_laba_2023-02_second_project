package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.structure.Department;

import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    List<Department> retrieveAll();
}
