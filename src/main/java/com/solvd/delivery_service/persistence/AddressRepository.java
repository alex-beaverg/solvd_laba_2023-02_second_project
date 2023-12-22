package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.area.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    void create(Address address);
    Optional<Address> findById(Long id);
    List<Address> findAll();
    void update(Address address, String field);
    void deleteById(Long id);
    Long countOfEntries();
}
