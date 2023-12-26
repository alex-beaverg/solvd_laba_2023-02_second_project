package com.solvd.delivery_service.persistence.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.persistence.ConnectionPool;
import com.solvd.delivery_service.persistence.PersonInfoRepository;
import com.solvd.delivery_service.service.DBService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonInfoRepositoryDaoImpl implements PersonInfoRepository {
    private static final DBService DB_SERVICE = DBService.getInstance();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_PERSON_QUERY =
            "INSERT INTO persons(first_name, last_name, age, passport_id, address_id) values(?, ?, ?, ?, ?);";
    private static final String FIND_PERSON_QUERY = "SELECT * FROM persons WHERE id = ?;";
    private static final String UPDATE_PERSON_FIRST_NAME_QUERY = "UPDATE persons SET first_name = ? WHERE id = ?;";
    private static final String UPDATE_PERSON_LAST_NAME_QUERY = "UPDATE persons SET last_name = ? WHERE id = ?;";
    private static final String UPDATE_PERSON_AGE_QUERY = "UPDATE persons SET age = ? WHERE id = ?;";
    private static final String DELETE_PERSON_QUERY = "DELETE FROM persons WHERE id = ?;";
    private static final String FIND_ALL_QUERY =
            "SELECT p.id AS person_id, p.first_name, p.last_name, p.age, ps.id AS passport_id, a.id AS address_id, " +
                    "ps.number AS passport, a.city, a.street, a.house, a.flat, a.zip_code, a.country " +
            "FROM persons p " +
            "JOIN passports ps ON p.passport_id = ps.id " +
            "JOIN addresses a ON p.address_id = a.id " +
            "ORDER BY p.id;";
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS persons_count FROM persons;";

    @Override
    public void create(PersonInfo personInfo) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PERSON_QUERY,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, personInfo.getFirstName());
            preparedStatement.setString(2, personInfo.getLastName());
            preparedStatement.setInt(3, personInfo.getAge());
            preparedStatement.setLong(4, personInfo.getPassport().getId());
            preparedStatement.setLong(5, personInfo.getAddress().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                personInfo.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create person!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<PersonInfo> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<PersonInfo> personInfoOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_PERSON_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            personInfoOptional = Optional.of(
                    new PersonInfo(resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            DB_SERVICE.getPassportRepository().findById(resultSet.getLong(4)).get(),
                            DB_SERVICE.getAddressRepository().findById(resultSet.getLong(5)).get()));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find person by id!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return personInfoOptional;
    }

    @Override
    public List<PersonInfo> findAll() {
        List<PersonInfo> persons;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            persons = mapPersons(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all persons!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return persons;
    }

    @Override
    public void update(PersonInfo personInfo, String field) {
        Connection connection = CONNECTION_POOL.getConnection();
        String query = null;
        String value = null;
        Class<?> clazz = String.class;
        switch (field) {
            case ("firstName") -> {
                query = UPDATE_PERSON_FIRST_NAME_QUERY;
                value = personInfo.getFirstName();
            }
            case ("lastName") -> {
                query = UPDATE_PERSON_LAST_NAME_QUERY;
                value = personInfo.getLastName();
            }
            case ("age") -> {
                query = UPDATE_PERSON_AGE_QUERY;
                clazz = Integer.class;
                value = String.valueOf(personInfo.getAge());
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (clazz.getSimpleName().equals(String.class.getSimpleName())) {
                preparedStatement.setString(1, value);
            } else {
                preparedStatement.setInt(1, Integer.parseInt(value));
            }
            preparedStatement.setLong(2, personInfo.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update person field!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERSON_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete person by id!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Long countOfEntries() {
        long count;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_OF_ENTRIES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get number of persons!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
    }

    private static List<PersonInfo> mapPersons(ResultSet resultSet) {
        List<PersonInfo> persons = new ArrayList<>();
        try {
            while (resultSet.next()) {
                PersonInfo person = new PersonInfo();
                person.setId(resultSet.getLong(1));
                person.setFirstName(resultSet.getString(2));
                person.setLastName(resultSet.getString(3));
                person.setAge(resultSet.getInt(4));
                person.setPassport(new Passport(
                        resultSet.getLong(5),
                        resultSet.getString(7)));
                person.setAddress(new Address(
                        resultSet.getLong(6),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        resultSet.getInt(10),
                        resultSet.getInt(11),
                        resultSet.getInt(12),
                        Country.valueOf(resultSet.getString(13))));
                persons.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map persons!", e);
        }
        return persons;
    }
}