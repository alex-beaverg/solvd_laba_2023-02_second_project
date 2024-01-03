package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee, Long departmentId);
    Employee retrieveById(Long id);
    List<Employee> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Employee employee, String field);
    void removeById(Long id);
    List<Employee> retrieveDepartmentEmployees(Department department);
}