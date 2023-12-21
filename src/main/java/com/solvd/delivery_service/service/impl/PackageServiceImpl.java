package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.persistence.PackageRepository;
import com.solvd.delivery_service.persistence.impl.PackageRepositoryImpl;
import com.solvd.delivery_service.service.AddressService;
import com.solvd.delivery_service.service.CustomerService;
import com.solvd.delivery_service.service.PackageService;

import java.util.List;

public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;
    private final AddressService addressService;
    private final CustomerService customerService;

    public PackageServiceImpl() {
        this.packageRepository = new PackageRepositoryImpl();
        this.addressService = new AddressServiceImpl();
        this.customerService = new CustomerServiceImpl();
    }

    @Override
    public Package create(Package pack) {
        pack.setId(null);
        if (pack.getAddressTo() != null) {
            Address addressTo = addressService.create(pack.getAddressTo());
            pack.setAddressTo(addressTo);
        }
        if (pack.getCustomer() != null) {
            Customer customer = customerService.create(pack.getCustomer());
            pack.setCustomer(customer);
        }
        packageRepository.create(pack);
        return pack;
    }

    @Override
    public List<Package> retrieveAll() {
        return packageRepository.findAll();
    }

    @Override
    public Long retrieveMaxPackageNumber() {
        return packageRepository.findMaxPackageNumber();
    }
}
