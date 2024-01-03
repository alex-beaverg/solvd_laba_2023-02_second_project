package com.solvd.delivery_service.persistence.basic_dao_impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.persistence.AddressRepository;
import com.solvd.delivery_service.persistence.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressRepositoryDaoImpl implements AddressRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_ADDRESS_QUERY =
            "INSERT INTO addresses(city, street, house, flat, zip_code, country) values(?, ?, ?, ?, ?, ?);";
    private static final String FIND_ADDRESS_QUERY = "SELECT * FROM addresses WHERE id = ?;";
    private static final String UPDATE_ADDRESS_CITY_QUERY = "UPDATE addresses SET city = ? WHERE id = ?;";
    private static final String UPDATE_ADDRESS_STREET_QUERY = "UPDATE addresses SET street = ? WHERE id = ?;";
    private static final String UPDATE_ADDRESS_HOUSE_QUERY = "UPDATE addresses SET house = ? WHERE id = ?;";
    private static final String UPDATE_ADDRESS_FLAT_QUERY = "UPDATE addresses SET flat = ? WHERE id = ?;";
    private static final String UPDATE_ADDRESS_ZIP_CODE_QUERY = "UPDATE addresses SET zip_code = ? WHERE id = ?;";
    private static final String UPDATE_ADDRESS_COUNTRY_QUERY = "UPDATE addresses SET country = ? WHERE id = ?;";
    private static final String DELETE_ADDRESS_QUERY = "DELETE FROM addresses WHERE id = ?;";
    private static final String FIND_ALL_QUERY =
            "SELECT a.id AS address_id, a.city, a.street, a.house, a.flat, a.zip_code, a.country " +
            "FROM addresses a " +
            "ORDER BY a.id;";
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS addresses_count FROM addresses;";
    private static final String FIND_MAX_ID = "SELECT MAX(id) FROM addresses;";

    @Override
    public void create(Address address) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ADDRESS_QUERY,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, address.getCity());
            preparedStatement.setString(2, address.getStreet());
            preparedStatement.setInt(3, address.getHouse());
            preparedStatement.setInt(4, address.getFlat());
            preparedStatement.setInt(5, address.getZipCode());
            preparedStatement.setString(6, address.getCountry().name());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                address.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create address!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Address> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Address> addressOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ADDRESS_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            addressOptional = Optional.of(
                    new Address(resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getInt(5),
                            resultSet.getInt(6),
                            Country.valueOf(resultSet.getString(7))));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find address by id!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return addressOptional;
    }

    @Override
    public List<Address> findAll() {
        List<Address> addresses;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            addresses = mapAddresses(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all addresses!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return addresses;
    }

    @Override
    public void update(Address address, String field) {
        Connection connection = CONNECTION_POOL.getConnection();
        String query = null;
        String value = null;
        Class<?> clazz = String.class;
        switch (field) {
            case ("city") -> {
                query = UPDATE_ADDRESS_CITY_QUERY;
                value = address.getCity();
            }
            case ("street") -> {
                query = UPDATE_ADDRESS_STREET_QUERY;
                value = address.getStreet();
            }
            case ("house") -> {
                query = UPDATE_ADDRESS_HOUSE_QUERY;
                clazz = Integer.class;
                value = String.valueOf(address.getHouse());
            }
            case ("flat") -> {
                query = UPDATE_ADDRESS_FLAT_QUERY;
                clazz = Integer.class;
                value = String.valueOf(address.getFlat());
            }
            case ("zipCode") -> {
                query = UPDATE_ADDRESS_ZIP_CODE_QUERY;
                clazz = Integer.class;
                value = String.valueOf(address.getZipCode());
            }
            case ("country") -> {
                query = UPDATE_ADDRESS_COUNTRY_QUERY;
                value = address.getCountry().name();
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (clazz.getSimpleName().equals(String.class.getSimpleName())) {
                preparedStatement.setString(1, value);
            } else {
                preparedStatement.setInt(1, Integer.parseInt(value));
            }
            preparedStatement.setLong(2, address.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update address field!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ADDRESS_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete address by id!", e);
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
            throw new RuntimeException("Unable to get number of addresses!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
    }

    @Override
    public Long findMaxId() {
        long id;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MAX_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            id = resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find max package id!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return id;
    }

    private static List<Address> mapAddresses(ResultSet resultSet) {
        List<Address> addresses = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Address address = new Address();
                address.setId(resultSet.getLong(1));
                address.setCity(resultSet.getString(2));
                address.setStreet(resultSet.getString(3));
                address.setHouse(resultSet.getInt(4));
                address.setFlat(resultSet.getInt(5));
                address.setZipCode(resultSet.getInt(6));
                address.setCountry(Country.valueOf(resultSet.getString(7)));
                addresses.add(address);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map addresses!", e);
        }
        return addresses;
    }
}