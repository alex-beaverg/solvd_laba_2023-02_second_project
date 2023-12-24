package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.persistence.AddressRepository;
import com.solvd.delivery_service.persistence.impl.AddressRepositoryDaoImpl;
import com.solvd.delivery_service.service.AddressService;

import java.util.List;

public class AddressServiceDaoImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceDaoImpl() {
        this.addressRepository = new AddressRepositoryDaoImpl();
    }

    @Override
    public Address create(Address address) {
        address.setId(null);
        addressRepository.create(address);
        return address;
    }

    @Override
    public List<Address> retrieveAll() {
        return addressRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return addressRepository.countOfEntries();
    }

    @Override
    public Long retrieveMaxId() {
        return addressRepository.findMaxId();
    }

    @Override
    public void updateField(Address address, String field) {
        addressRepository.update(address, field);
    }
}
