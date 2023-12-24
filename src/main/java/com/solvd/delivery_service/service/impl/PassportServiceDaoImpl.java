package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.persistence.PassportRepository;
import com.solvd.delivery_service.persistence.impl.PassportRepositoryDaoImpl;
import com.solvd.delivery_service.service.PassportService;

import java.util.List;

public class PassportServiceDaoImpl implements PassportService {
    private final PassportRepository passportRepository;

    public PassportServiceDaoImpl() {
        this.passportRepository = new PassportRepositoryDaoImpl();
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

    @Override
    public void updateField(Passport passport) {
        passportRepository.update(passport);
    }
}
