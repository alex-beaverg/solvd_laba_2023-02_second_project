package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.area.Address;

import java.util.List;

public interface AddressService {
    Address create(Address address);
    List<Address> retrieveAll();
}
