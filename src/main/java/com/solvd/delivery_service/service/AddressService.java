package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.area.Address;

import java.util.List;

public interface AddressService {
    Address create(Address address);
    Address retrieveById(Long id);
    List<Address> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Address address, String field);
    void removeById(Long id);
}