package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.pack.Package;

import java.util.List;
import java.util.Optional;

public interface PackageRepository {
    void create(Package pack);
    Optional<Package> findById(Long id);
    List<Package> findAll();
    void update(Package pack, String field);
    void deleteById(Long id);
    Long findMaxPackageNumber();
    Long countOfEntries();
}
