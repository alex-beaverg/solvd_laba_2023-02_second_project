package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.pack.Package;

import java.util.List;

public interface PackageService {
    Package create(Package pack);
    Package createWithExistCustomer(Package pack);
    List<Package> retrieveAll();
    Long retrieveMaxPackageNumber();
    Long retrieveNumberOfEntries();
    void removeById(Long id);
    void updateField(Package pack, String field);
}
