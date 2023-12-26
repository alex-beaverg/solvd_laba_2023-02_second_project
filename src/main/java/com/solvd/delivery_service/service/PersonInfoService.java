package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.PersonInfo;

import java.util.List;

public interface PersonInfoService {
    PersonInfo create(PersonInfo personInfo);
    PersonInfo retrieveById(Long id);
    List<PersonInfo> retrieveAll();
    Long retrieveNumberOfEntries();
    void updateField(PersonInfo personInfo, String field);
    void removeById(Long id);
}