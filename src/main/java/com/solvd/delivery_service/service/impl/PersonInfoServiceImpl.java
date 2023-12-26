package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.persistence.PersonInfoRepository;
import com.solvd.delivery_service.service.AddressService;
import com.solvd.delivery_service.service.DBService;
import com.solvd.delivery_service.service.PassportService;
import com.solvd.delivery_service.service.PersonInfoService;

import java.util.List;

public class PersonInfoServiceImpl implements PersonInfoService {
    private static final DBService DB_SERVICE = DBService.getInstance();
    private final PersonInfoRepository personInfoRepository;
    private final PassportService passportService;
    private final AddressService addressService;

    public PersonInfoServiceImpl() {
        this.personInfoRepository = DB_SERVICE.getPersonInfoRepository();
        this.passportService = new PassportServiceImpl();
        this.addressService = new AddressServiceImpl();
    }

    @Override
    public PersonInfo create(PersonInfo personInfo) {
        personInfo.setId(null);
        if (personInfo.getPassport() != null) {
            Passport passport = passportService.create(personInfo.getPassport());
            personInfo.setPassport(passport);
        }
        if (personInfo.getAddress() != null) {
            Address address = addressService.create(personInfo.getAddress());
            personInfo.setAddress(address);
        }
        personInfoRepository.create(personInfo);
        return personInfo;
    }

    @Override
    public PersonInfo retrieveById(Long id) {
        return personInfoRepository.findById(id).get();
    }

    @Override
    public List<PersonInfo> retrieveAll() {
        return personInfoRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return personInfoRepository.countOfEntries();
    }

    @Override
    public void updateField(PersonInfo personInfo, String field) {
        personInfoRepository.update(personInfo, field);
    }

    @Override
    public void removeById(Long id) {
        personInfoRepository.deleteById(id);
    }
}