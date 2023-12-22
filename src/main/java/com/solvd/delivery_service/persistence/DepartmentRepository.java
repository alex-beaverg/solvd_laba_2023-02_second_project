package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.structure.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {
    void create(Department department);
    Optional<Department> findById(Long id);
    List<Department> findAll();
    void update(Department department);
    void deleteById(Long id);
    Long countOfEntries();
}
