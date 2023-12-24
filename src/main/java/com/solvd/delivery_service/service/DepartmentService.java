package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;

import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    List<Department> retrieveAll();
    Long retrieveNumberOfEntries();
    void removeById(Long id);
    void rename(Department department);
    List<Employee> retrieveDepartmentEmployees(Department department);
    Department retrieveById(Long id);
}
