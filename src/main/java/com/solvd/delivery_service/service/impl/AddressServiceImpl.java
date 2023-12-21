package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.persistence.AddressRepository;
import com.solvd.delivery_service.persistence.impl.AddressRepositoryImpl;
import com.solvd.delivery_service.service.AddressService;

import java.util.List;

public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceImpl() {
        this.addressRepository = new AddressRepositoryImpl();
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
}
