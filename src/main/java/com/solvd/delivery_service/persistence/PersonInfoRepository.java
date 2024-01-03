package com.solvd.delivery_service.persistence;

import com.solvd.delivery_service.domain.human.PersonInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PersonInfoRepository {
    void create(@Param("personInfo") PersonInfo personInfo);
    Optional<PersonInfo> findById(@Param("id") Long id);
    List<PersonInfo> findAll();
    void update(@Param("personInfo") PersonInfo personInfo, @Param("field") String field);
    void deleteById(@Param("id") Long id);
    Long countOfEntries();
}