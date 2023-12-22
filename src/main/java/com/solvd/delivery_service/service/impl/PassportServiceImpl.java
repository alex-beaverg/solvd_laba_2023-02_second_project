package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.persistence.PassportRepository;
import com.solvd.delivery_service.persistence.impl.PassportRepositoryImpl;
import com.solvd.delivery_service.service.PassportService;

import java.util.List;

public class PassportServiceImpl implements PassportService {
    private final PassportRepository passportRepository;

    public PassportServiceImpl() {
        this.passportRepository = new PassportRepositoryImpl();
    }

    @Override
    public Passport create(Passport passport) {
        passport.setId(null);
        passportRepository.create(passport);
        return passport;
    }

    @Override
    public List<Passport> retrieveAll() {
        return passportRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return passportRepository.countOfEntries();
    }
}
