package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.Passport;

import java.util.List;

public interface PassportService {
    Passport create(Passport passport);
    Passport retrieveById(Long id);
    List<Passport> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Passport passport);
    void removeById(Long id);
}