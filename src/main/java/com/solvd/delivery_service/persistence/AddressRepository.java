package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.area.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    void create(@Param("address") Address address);
    Optional<Address> findById(@Param("id") Long id);
    List<Address> findAll();
    void update(@Param("address") Address address, @Param("field") String field);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
    Long findMaxId();
}