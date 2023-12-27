package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.persistence.AddressRepository;
import com.solvd.delivery_service.service.AddressService;
import com.solvd.delivery_service.service.DaoService;

import java.util.List;

public class AddressServiceImpl implements AddressService {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private final AddressRepository addressRepository;

    public AddressServiceImpl() {
        this.addressRepository = DAO_SERVICE.getAddressRepository();
    }

    @Override
    public Address create(Address address) {
        address.setId(null);
        addressRepository.create(address);
        return address;
    }

    @Override
    public Address retrieveById(Long id) {
        return addressRepository.findById(id).get();
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
    public void updateField(Address address, String field) {
        addressRepository.update(address, field);
    }

    @Override
    public void removeById(Long id) {
        addressRepository.deleteById(id);
    }
}