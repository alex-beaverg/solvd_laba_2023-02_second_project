package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.persistence.MybatisConfig;
import com.solvd.delivery_service.persistence.PersonInfoRepository;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class PersonInfoRepositoryMybatisImpl implements PersonInfoRepository {
    @Override
    public void create(PersonInfo personInfo) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PersonInfoRepository personInfoRepository = sqlSession.getMapper(PersonInfoRepository.class);
            personInfoRepository.create(personInfo);
        }
    }

    @Override
    public Optional<PersonInfo> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PersonInfoRepository personInfoRepository = sqlSession.getMapper(PersonInfoRepository.class);
            return personInfoRepository.findById(id);
        }
    }

    @Override
    public List<PersonInfo> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PersonInfoRepository personInfoRepository = sqlSession.getMapper(PersonInfoRepository.class);
            return personInfoRepository.findAll();
        }
    }

    @Override
    public void update(PersonInfo personInfo, String field) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PersonInfoRepository personInfoRepository = sqlSession.getMapper(PersonInfoRepository.class);
            personInfoRepository.update(personInfo, field);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PersonInfoRepository personInfoRepository = sqlSession.getMapper(PersonInfoRepository.class);
            personInfoRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PersonInfoRepository personInfoRepository = sqlSession.getMapper(PersonInfoRepository.class);
            return personInfoRepository.countOfEntries();
        }
    }
}