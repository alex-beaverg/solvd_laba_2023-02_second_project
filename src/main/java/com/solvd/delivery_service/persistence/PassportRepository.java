package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.Passport;

import java.util.List;
import java.util.Optional;

public interface PassportRepository {
    void create(Passport passport);
    Optional<Passport> findById(Long id);
    List<Passport> findAll();
    void update(Passport passport);
    void deleteById(Long id);
}
