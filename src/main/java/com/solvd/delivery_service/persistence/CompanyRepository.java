package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.structure.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    void create(@Param("company") Company company);
    Optional<Company> findById(@Param("id") Long id);
    List<Company> findAll();
    void update(@Param("company") Company company);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
}
