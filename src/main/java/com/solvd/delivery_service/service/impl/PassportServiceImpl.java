package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.persistence.PassportRepository;
import com.solvd.delivery_service.util.console_menu.DaoService;
import com.solvd.delivery_service.service.PassportService;

import java.util.List;

public class PassportServiceImpl implements PassportService {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private final PassportRepository passportRepository;

    public PassportServiceImpl() {
        this.passportRepository = DAO_SERVICE.getRepository(PassportRepository.class);
    }

    @Override
    public Passport create(Passport passport) {
        passport.setId(null);
        passportRepository.create(passport);
        return passport;
    }

    @Override
    public Passport retrieveById(Long id) {
        return passportRepository.findById(id).get();
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

    @Override
    public void removeById(Long id) {
        passportRepository.deleteById(id);
    }
}