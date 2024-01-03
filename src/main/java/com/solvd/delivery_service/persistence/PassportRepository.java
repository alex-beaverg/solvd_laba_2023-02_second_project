package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.Passport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PassportRepository {
    void create(@Param("passport") Passport passport);
    Optional<Passport> findById(@Param("id") Long id);
    List<Passport> findAll();
    void update(@Param("passport") Passport passport);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
}