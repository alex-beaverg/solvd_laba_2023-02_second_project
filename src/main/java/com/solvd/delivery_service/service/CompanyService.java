package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.structure.Company;

import java.util.List;

public interface CompanyService {
    Company create(Company company);
    Company retrieveById(Long id);
    List<Company> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Company company);
    void removeById(Long id);
}
