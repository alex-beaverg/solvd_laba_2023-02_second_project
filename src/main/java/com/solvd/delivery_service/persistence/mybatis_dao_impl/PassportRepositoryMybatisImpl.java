package com.solvd.delivery_service.persistence.mybatis_dao_impl;

import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.persistence.MybatisConfig;
import com.solvd.delivery_service.persistence.PassportRepository;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class PassportRepositoryMybatisImpl implements PassportRepository {
    @Override
    public void create(Passport passport) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PassportRepository passportRepository = sqlSession.getMapper(PassportRepository.class);
            passportRepository.create(passport);
        }
    }

    @Override
    public Optional<Passport> findById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PassportRepository passportRepository = sqlSession.getMapper(PassportRepository.class);
            return passportRepository.findById(id);
        }
    }

    @Override
    public List<Passport> findAll() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PassportRepository passportRepository = sqlSession.getMapper(PassportRepository.class);
            return passportRepository.findAll();
        }
    }

    @Override
    public void update(Passport passport) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PassportRepository passportRepository = sqlSession.getMapper(PassportRepository.class);
            passportRepository.update(passport);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PassportRepository passportRepository = sqlSession.getMapper(PassportRepository.class);
            passportRepository.deleteById(id);
        }
    }

    @Override
    public Long countOfEntries() {
        try (SqlSession sqlSession = MybatisConfig.getSessionFactory().openSession(true)) {
            PassportRepository passportRepository = sqlSession.getMapper(PassportRepository.class);
            return passportRepository.countOfEntries();
        }
    }
}