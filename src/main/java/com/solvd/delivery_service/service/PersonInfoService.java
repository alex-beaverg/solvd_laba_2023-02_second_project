package com.solvd.delivery_service.service;

import com.solvd.delivery_service.domain.human.PersonInfo;

import java.util.List;

public interface PersonInfoService {
    PersonInfo create(PersonInfo personInfo);
    List<PersonInfo> retrieveAll();
    Long retrieveNumberOfEntries();
}
