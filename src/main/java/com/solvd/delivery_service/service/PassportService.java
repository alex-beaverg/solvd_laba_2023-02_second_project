package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.Passport;

import java.util.List;

public interface PassportService {
    Passport create(Passport passport);
    List<Passport> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(Passport passport);
}
