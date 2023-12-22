package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.PersonInfo;

import java.util.List;
import java.util.Optional;

public interface PersonInfoRepository {
    void create(PersonInfo personInfo);
    Optional<PersonInfo> findById(Long id);
    List<PersonInfo> findAll();
    void update(PersonInfo personInfo, String field);
    void deleteById(Long id);
    Long countOfEntries();
}
