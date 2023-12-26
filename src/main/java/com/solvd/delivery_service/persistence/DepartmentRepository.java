package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.structure.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {
    void create(@Param("department") Department department);
    Optional<Department> findById(@Param("id") Long id);
    List<Department> findAll();
    void update(@Param("department") Department department);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
}