package com.solvd.delivery_service.persistence.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.persistence.ConnectionPool;
import com.solvd.delivery_service.persistence.CustomerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_CUSTOMER_QUERY = "INSERT INTO customers(person_id) values(?);";
    private static final String FIND_CUSTOMER_QUERY = "SELECT * FROM customers WHERE id = ?;";
    private static final String UPDATE_CUSTOMER_PERSON_ID_QUERY = "UPDATE customers SET person_id = ? WHERE id = ?;";
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM customers WHERE id = ?;";
    private static final String FIND_ALL_QUERY =
            "SELECT cu.id AS customer_id, p.id AS person_id, p.first_name, p.last_name, p.age, " +
                    "ps.id AS passport_id, a.id AS address_id, ps.number AS passport, a.city, a.street, a.house, " +
                    "a.flat, a.zip_code, a.country " +
            "FROM customers cu " +
            "JOIN persons p ON cu.person_id = p.id " +
            "JOIN passports ps ON p.passport_id = ps.id " +
            "JOIN addresses a ON p.address_id = a.id " +
            "ORDER BY cu.id;";

    @Override
    public void create(Customer customer) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, customer.getPersonInfo().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                customer.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create customer!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Customer> customerOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            customerOptional = Optional.of(
                    new Customer(resultSet.getLong(1),
                            new PersonInfoRepositoryImpl().findById(resultSet.getLong(2)).get()));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find customer!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return customerOptional;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            customers = mapCustomers(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all customers!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return customers;
    }

    @Override
    public void update(Customer customer) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_PERSON_ID_QUERY)) {
            preparedStatement.setLong(1, customer.getPersonInfo().getId());
            preparedStatement.setLong(2, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update customer!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete customer!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    private static List<Customer> mapCustomers(ResultSet resultSet) {
        List<Customer> customers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getLong(1));
                customer.setPersonInfo(new PersonInfo(
                        resultSet.getLong(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        new Passport(
                                resultSet.getLong(6),
                                resultSet.getString(8)),
                        new Address(
                                resultSet.getLong(7),
                                resultSet.getString(9),
                                resultSet.getString(10),
                                resultSet.getInt(11),
                                resultSet.getInt(12),
                                resultSet.getInt(13),
                                Country.valueOf(resultSet.getString(14)))));
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map customers!", e);
        }
        return customers;
    }
}