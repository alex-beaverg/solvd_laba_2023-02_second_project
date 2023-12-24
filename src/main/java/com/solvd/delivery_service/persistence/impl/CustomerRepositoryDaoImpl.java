package com.solvd.delivery_service.persistence.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.human.employee.Experience;
import com.solvd.delivery_service.domain.human.employee.Position;
import com.solvd.delivery_service.domain.pack.*;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.ConnectionPool;
import com.solvd.delivery_service.persistence.CustomerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryDaoImpl implements CustomerRepository {
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
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS customers_count FROM customers;";
    private static final String FIND_CUSTOMERS_BY_LAST_NAME_QUERY =
            "SELECT c.id AS customer_id, p.id AS person_id, p.first_name, p.last_name, p.age, ps.id AS passport_id, " +
                    "a.id AS address_id, ps.number AS passport, a.city, a.street, a.house, a.flat, a.zip_code, a.country " +
            "FROM customers c " +
            "JOIN persons p ON c.person_id = p.id " +
            "JOIN addresses a ON p.address_id = a.id " +
            "JOIN passports ps ON p.passport_id = ps.id " +
            "WHERE p.last_name = ?;";
    private static final String FIND_CUSTOMER_PACKAGES_QUERY =
            "SELECT pg.id AS package_id, pg.number, pg.package_type, pg.delivery_type, pg.status, pg.package_condition, " +
                    "a3.id AS address_from_id, a3.city AS city_from, a3.street AS street_from, a3.house AS house_from, " +
                    "a3.flat AS flat_from, a3.zip_code AS zip_code_from, a3.country AS country_from, a4.id AS address_to_id, " +
                    "a4.city AS city_to, a4.street AS street_to, " +
                    "a4.house AS house_to, a4.flat AS flat_to, a4.zip_code AS zip_code_to, a4.country AS country_to, " +
                    "cu.id AS customer_id, p2.id AS customer_person_id, " +
                    "p2.first_name AS customer_first_name, p2.last_name AS customer_last_name, p2.age AS customer_age, " +
                    "ps2.id AS customer_passport_id, ps2.number AS customer_passport, a2.id AS customer_address_id, " +
                    "a2.city AS customer_city, a2.street AS customer_street, a2.house AS customer_house, " +
                    "a2.flat AS customer_flat, a2.zip_code AS customer_zip_code, a2.country AS customer_country, " +
                    "e.id AS employee_id, e.position, e.experience, p1.id AS employee_person_id, " +
                    "p1.first_name AS employee_first_name, p1.last_name AS employee_last_name, p1.age AS employee_age, " +
                    "ps1.id AS employee_passport_id, ps1.number AS employee_passport, a1.id AS employee_address_id, " +
                    "a1.city AS employee_city, a1.street AS employee_street, a1.house AS employee_house, " +
                    "a1.flat AS employee_flat, a1.zip_code AS employee_zip_code, a1.country AS employee_country, " +
                    "d.id AS employee_department_id, d.title AS employee_department " +
                    "FROM packages pg " +
                    "JOIN employees e ON pg.employee_id = e.id " +
                    "JOIN persons p1 ON e.person_id = p1.id " +
                    "JOIN departments d ON e.department_id = d.id " +
                    "JOIN passports ps1 ON p1.passport_id = ps1.id " +
                    "JOIN addresses a1 ON p1.address_id = a1.id " +
                    "JOIN customers cu ON pg.customer_id = cu.id " +
                    "JOIN persons p2 ON cu.person_id = p2.id " +
                    "JOIN passports ps2 ON p2.passport_id = ps2.id " +
                    "JOIN addresses a2 ON p2.address_id = a2.id " +
                    "JOIN addresses a3 ON pg.address_from_id = a3.id " +
                    "JOIN addresses a4 ON pg.address_to_id = a4.id " +
                    "WHERE customer_id = ? " +
                    "ORDER BY pg.id;";

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
                            new PersonInfoRepositoryDaoImpl().findById(resultSet.getLong(2)).get()));
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
        List<Package> packages;
        connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_PACKAGES_QUERY)) {
            int index = 1;
            for (Customer customer : customers) {
                preparedStatement.setLong(1, index);
                ResultSet resultSet = preparedStatement.executeQuery();
                packages = mapPackages(resultSet);
                customer.setPackages(packages);
                index++;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find customers by last name!", e);
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

    @Override
    public Long countOfEntries() {
        Long count = 0L;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_OF_ENTRIES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get count of customers!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        List<Customer> customers;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_BY_LAST_NAME_QUERY)) {
            preparedStatement.setString(1, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            customers = mapCustomers(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find customers by last name!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return customers;
    }

    @Override
    public List<Package> findCustomerPackages(Customer customer) {
        List<Package> packages;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_PACKAGES_QUERY)) {
            preparedStatement.setLong(1, customer.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            packages = mapPackages(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find customers by last name!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return packages;
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

    private static List<Package> mapPackages(ResultSet resultSet) {
        List<Package> packages = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Package pack = new Package();
                pack.setId(resultSet.getLong(1));
                pack.setNumber(resultSet.getLong(2));
                pack.setPackageType(PackageType.valueOf(resultSet.getString(3)));
                pack.setDeliveryType(DeliveryType.valueOf(resultSet.getString(4)));
                pack.setStatus(Status.valueOf(resultSet.getString(5)));
                pack.setCondition(Condition.valueOf(resultSet.getString(6)));
                pack.setAddressFrom(new Address(
                        resultSet.getLong(7),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        resultSet.getInt(10),
                        resultSet.getInt(11),
                        resultSet.getInt(12),
                        Country.valueOf(resultSet.getString(13))));
                pack.setAddressTo(new Address(
                        resultSet.getLong(14),
                        resultSet.getString(15),
                        resultSet.getString(16),
                        resultSet.getInt(17),
                        resultSet.getInt(18),
                        resultSet.getInt(19),
                        Country.valueOf(resultSet.getString(20))));
                pack.setCustomer(new Customer(
                        resultSet.getLong(21),
                        new PersonInfo(
                                resultSet.getLong(22),
                                resultSet.getString(23),
                                resultSet.getString(24),
                                resultSet.getInt(25),
                                new Passport(
                                        resultSet.getLong(26),
                                        resultSet.getString(27)),
                                new Address(
                                        resultSet.getLong(28),
                                        resultSet.getString(29),
                                        resultSet.getString(30),
                                        resultSet.getInt(31),
                                        resultSet.getInt(32),
                                        resultSet.getInt(33),
                                        Country.valueOf(resultSet.getString(34))))));
                pack.setEmployee(new Employee(
                        resultSet.getLong(35),
                        Position.valueOf(resultSet.getString(36)),
                        Experience.valueOf(resultSet.getString(37)),
                        new Department(
                                resultSet.getLong(51),
                                resultSet.getString(52)),
                        new PersonInfo(
                                resultSet.getLong(38),
                                resultSet.getString(39),
                                resultSet.getString(40),
                                resultSet.getInt(41),
                                new Passport(
                                        resultSet.getLong(42),
                                        resultSet.getString(43)),
                                new Address(
                                        resultSet.getLong(44),
                                        resultSet.getString(45),
                                        resultSet.getString(46),
                                        resultSet.getInt(47),
                                        resultSet.getInt(48),
                                        resultSet.getInt(49),
                                        Country.valueOf(resultSet.getString(50))))));
                packages.add(pack);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map packages!", e);
        }
        return packages;
    }
}
