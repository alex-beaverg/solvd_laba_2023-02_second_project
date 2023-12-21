package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.pack.Package;

import java.util.List;

public interface PackageService {
    Package create(Package pack);
    List<Package> retrieveAll();
    Long retrieveMaxPackageNumber();
}
