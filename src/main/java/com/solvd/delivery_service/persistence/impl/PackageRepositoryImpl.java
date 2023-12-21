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
import com.solvd.delivery_service.persistence.PackageRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackageRepositoryImpl implements PackageRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_PACKAGE_QUERY =
            "INSERT INTO packages(number, package_type, delivery_type, status, package_condition, address_from_id, " +
                    "address_to_id, customer_id, employee_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String FIND_PACKAGE_QUERY = "SELECT * FROM packages WHERE id = ?;";
    private static final String UPDATE_PACKAGE_NUMBER_QUERY = "UPDATE packages SET number = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_TYPE_QUERY = "UPDATE packages SET package_type = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_DELIVERY_TYPE_QUERY = "UPDATE packages SET delivery_type = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_STATUS_QUERY = "UPDATE packages SET status = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_CONDITION_QUERY = "UPDATE packages SET package_condition = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_ADDRESS_FROM_ID_QUERY = "UPDATE packages SET address_from_id = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_ADDRESS_TO_ID_QUERY = "UPDATE packages SET address_to_id = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_CUSTOMER_ID_QUERY = "UPDATE packages SET customer_id = ? WHERE id = ?;";
    private static final String UPDATE_PACKAGE_EMPLOYEE_ID_QUERY = "UPDATE packages SET employee_id = ? WHERE id = ?;";
    private static final String DELETE_PACKAGE_QUERY = "DELETE FROM packages WHERE id = ?;";
    private static final String FIND_ALL_QUERY =
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
            "ORDER BY pg.id;";
    private static final String FIND_MAX_PACKAGE_NUMBER = "SELECT MAX(number) FROM packages;";

    @Override
    public void create(Package pack) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PACKAGE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, pack.getNumber());
            preparedStatement.setString(2, pack.getPackageType().name());
            preparedStatement.setString(3, pack.getDeliveryType().name());
            preparedStatement.setString(4, pack.getStatus().name());
            preparedStatement.setString(5, pack.getCondition().name());
            preparedStatement.setLong(6, pack.getAddressFrom().getId());
            preparedStatement.setLong(7, pack.getAddressTo().getId());
            preparedStatement.setLong(8, pack.getCustomer().getId());
            preparedStatement.setLong(9, pack.getEmployee().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                pack.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create package!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Package> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Package> packageOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_PACKAGE_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            packageOptional = Optional.of(
                    new Package(
                            resultSet.getLong(1),
                            PackageType.valueOf(resultSet.getString(2)),
                            DeliveryType.valueOf(resultSet.getString(3)),
                            Status.valueOf(resultSet.getString(4)),
                            Condition.valueOf(resultSet.getString(5)),
                            new AddressRepositoryImpl().findById(resultSet.getLong(6)).get(),
                            new AddressRepositoryImpl().findById(resultSet.getLong(7)).get(),
                            new CustomerRepositoryImpl().findById(resultSet.getLong(8)).get(),
                            new EmployeeRepositoryImpl().findById(resultSet.getLong(9)).get()));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find package!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return packageOptional;
    }

    @Override
    public List<Package> findAll() {
        List<Package> packages;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            packages = mapPackages(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all packages!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return packages;
    }

    @Override
    public void update(Package pack, String field) {
        Connection connection = CONNECTION_POOL.getConnection();
        String query = null;
        String value = null;
        Class<?> clazz = String.class;
        switch (field) {
            case ("number") -> {
                query = UPDATE_PACKAGE_NUMBER_QUERY;
                clazz = Long.class;
                value = String.valueOf(pack.getNumber());
            }
            case ("package_type") -> {
                query = UPDATE_PACKAGE_TYPE_QUERY;
                value = pack.getPackageType().name();
            }
            case ("delivery_type") -> {
                query = UPDATE_PACKAGE_DELIVERY_TYPE_QUERY;
                value = pack.getDeliveryType().name();
            }
            case ("status") -> {
                query = UPDATE_PACKAGE_STATUS_QUERY;
                value = pack.getStatus().name();
            }
            case ("package_condition") -> {
                query = UPDATE_PACKAGE_CONDITION_QUERY;
                value = pack.getCondition().name();
            }
            case ("address_from_id") -> {
                query = UPDATE_PACKAGE_ADDRESS_FROM_ID_QUERY;
                clazz = Long.class;
                value = String.valueOf(pack.getAddressFrom().getId());
            }
            case ("address_to_id") -> {
                query = UPDATE_PACKAGE_ADDRESS_TO_ID_QUERY;
                clazz = Long.class;
                value = String.valueOf(pack.getAddressTo().getId());
            }
            case ("customer_id") -> {
                query = UPDATE_PACKAGE_CUSTOMER_ID_QUERY;
                clazz = Long.class;
                value = String.valueOf(pack.getCustomer().getId());
            }
            case ("employee_id") -> {
                query = UPDATE_PACKAGE_EMPLOYEE_ID_QUERY;
                clazz = Long.class;
                value = String.valueOf(pack.getEmployee().getId());
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (clazz.getSimpleName().equals(String.class.getSimpleName())) {
                preparedStatement.setString(1, value);
            } else {
                preparedStatement.setLong(1, Long.parseLong(value));
            }
            preparedStatement.setLong(2, pack.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update package!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PACKAGE_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete package!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
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

    @Override
    public Long findMaxPackageNumber() {
        Long number;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MAX_PACKAGE_NUMBER)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            number = resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find max package number!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return number;
    }
}
