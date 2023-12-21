package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.employee.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);
    List<Employee> retrieveAll();
}
