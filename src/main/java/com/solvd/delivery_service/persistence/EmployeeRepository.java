package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.employee.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    void create(Employee employee);
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    void update(Employee employee, String field);
    void deleteById(Long id);
    Long countOfEntries();
}
