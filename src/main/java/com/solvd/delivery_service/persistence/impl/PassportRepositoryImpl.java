package com.solvd.delivery_service.persistence.impl;

import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.persistence.ConnectionPool;
import com.solvd.delivery_service.persistence.PassportRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassportRepositoryImpl implements PassportRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_PASSPORT_QUERY = "INSERT INTO passports(number) values(?);";
    private static final String FIND_PASSPORT_QUERY = "SELECT * FROM passports WHERE id = ?;";
    private static final String UPDATE_PASSPORT_QUERY = "UPDATE passports SET number = ? WHERE id = ?;";
    private static final String DELETE_PASSPORT_QUERY = "DELETE FROM passports WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM passports ORDER BY id;";

    @Override
    public void create(Passport passport) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PASSPORT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, passport.getNumber());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                passport.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create passport!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Passport> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Passport> passportOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_PASSPORT_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            passportOptional = Optional.of(new Passport(resultSet.getLong(1), resultSet.getString(2)));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find passport!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return passportOptional;
    }

    @Override
    public List<Passport> findAll() {
        List<Passport> passports;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            passports = mapPassports(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all passports!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return passports;
    }

    @Override
    public void update(Passport passport) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSPORT_QUERY)) {
            preparedStatement.setString(1, passport.getNumber());
            preparedStatement.setLong(2, passport.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update passport!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PASSPORT_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete passport!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    private static List<Passport> mapPassports(ResultSet resultSet) {
        List<Passport> passports = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Passport passport = new Passport();
                passport.setId(resultSet.getLong(1));
                passport.setNumber(resultSet.getString(2));
                passports.add(passport);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map passports!", e);
        }
        return passports;
    }
}
