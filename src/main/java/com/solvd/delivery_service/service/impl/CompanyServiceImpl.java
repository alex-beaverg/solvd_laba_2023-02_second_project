package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.persistence.CompanyRepository;
import com.solvd.delivery_service.service.CompanyService;
import com.solvd.delivery_service.util.console_menu.DaoService;

import java.util.List;

public class CompanyServiceImpl implements CompanyService {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl() {
        this.companyRepository = DAO_SERVICE.getRepository(CompanyRepository.class);
    }

    @Override
    public Company create(Company company) {
        company.setId(null);
        companyRepository.create(company);
        return company;
    }

    @Override
    public Company retrieveById(Long id) {
        return companyRepository.findById(id).get();
    }

    @Override
    public List<Company> retrieveAll() {
        return companyRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return companyRepository.countOfEntries();
    }

    @Override
    public void updateField(Company company) {
        companyRepository.update(company);
    }

    @Override
    public void removeById(Long id) {
        companyRepository.deleteById(id);
    }
}
