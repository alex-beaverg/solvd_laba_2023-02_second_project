package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    void create(@Param("employee") Employee employee, @Param("departmentId") Long departmentId);
    Optional<Employee> findById(@Param("id") Long id);
    List<Employee> findAll();
    void update(@Param("employee") Employee employee, @Param("field") String field);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
    List<Employee> findDepartmentEmployees(@Param("department") Department department);
}