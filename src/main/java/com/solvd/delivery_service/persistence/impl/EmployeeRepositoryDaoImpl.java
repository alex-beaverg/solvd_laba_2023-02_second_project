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
import com.solvd.delivery_service.persistence.EmployeeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryDaoImpl implements EmployeeRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_EMPLOYEE_QUERY =
            "INSERT INTO employees(position, experience, department_id, person_id) values(?, ?, ?, ?);";
    private static final String FIND_EMPLOYEE_QUERY = "SELECT * FROM employees WHERE id = ?;";
    private static final String UPDATE_EMPLOYEE_POSITION_QUERY = "UPDATE employees SET position = ? WHERE id = ?;";
    private static final String UPDATE_EMPLOYEE_EXPERIENCE_QUERY = "UPDATE employees SET experience = ? WHERE id = ?;";
    private static final String DELETE_EMPLOYEE_QUERY = "DELETE FROM employees WHERE id = ?;";
    private static final String FIND_ALL_QUERY =
            "SELECT e.id AS employee_id, e.position, e.experience, d.id AS department_id, p.id AS person_id, " +
                    "d.title AS department, p.first_name, p.last_name, p.age, ps.id AS passport_id, " +
                    "a.id AS address_id, ps.number AS passport, a.city, a.street, a.house, a.flat, " +
                    "a.zip_code, a.country " +
            "FROM employees e " +
            "JOIN persons p ON e.person_id = p.id " +
            "JOIN departments d ON e.department_id = d.id " +
            "JOIN passports ps ON p.passport_id = ps.id " +
            "JOIN addresses a ON p.address_id = a.id " +
            "ORDER BY e.id;";
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS employees_count FROM employees;";
    private static final String FIND_EMPLOYEE_PACKAGES_QUERY =
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
                    "WHERE employee_id = ? " +
                    "ORDER BY pg.id;";

    @Override
    public void create(Employee employee) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, employee.getPosition().name());
            preparedStatement.setString(2, employee.getExperience().name());
            preparedStatement.setLong(3, employee.getDepartment().getId());
            preparedStatement.setLong(4, employee.getPersonInfo().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                employee.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create employee!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Employee> employeeOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMPLOYEE_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            employeeOptional = Optional.of(
                    new Employee(
                            Position.valueOf(resultSet.getString(1)),
                            Experience.valueOf(resultSet.getString(2)),
                            new DepartmentRepositoryDaoImpl().findById(resultSet.getLong(3)).get(),
                            new PersonInfoRepositoryDaoImpl().findById(resultSet.getLong(4)).get()));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find employee!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return employeeOptional;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            employees = mapEmployees(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all employees!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        List<Package> packages;
        connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMPLOYEE_PACKAGES_QUERY)) {
            int index = 1;
            for (Employee employee : employees) {
                preparedStatement.setLong(1, index);
                ResultSet resultSet = preparedStatement.executeQuery();
                packages = mapPackages(resultSet);
                employee.setPackages(packages);
                index++;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find customers by last name!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return employees;
    }

    @Override
    public void update(Employee employee, String field) {
        Connection connection = CONNECTION_POOL.getConnection();
        String query = null;
        String value = null;
        switch (field) {
            case ("position") -> {
                query = UPDATE_EMPLOYEE_POSITION_QUERY;
                value = employee.getPosition().name();
            }
            case ("experience") -> {
                query = UPDATE_EMPLOYEE_EXPERIENCE_QUERY;
                value = employee.getExperience().name();
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, value);
            preparedStatement.setLong(2, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update employee!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete employee!", e);
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
            throw new RuntimeException("Unable to get count of employees!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
    }

    private static List<Employee> mapEmployees(ResultSet resultSet) {
        List<Employee> employees = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setPosition(Position.valueOf(resultSet.getString(2)));
                employee.setExperience(Experience.valueOf(resultSet.getString(3)));
                employee.setDepartment(
                        new Department(
                            resultSet.getLong(4),
                            resultSet.getString(6)));
                employee.setPersonInfo(
                        new PersonInfo(
                            resultSet.getLong(5),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getInt(9),
                            new Passport(
                                    resultSet.getLong(10),
                                    resultSet.getString(12)),
                            new Address(
                                    resultSet.getLong(11),
                                    resultSet.getString(13),
                                    resultSet.getString(14),
                                    resultSet.getInt(15),
                                    resultSet.getInt(16),
                                    resultSet.getInt(17),
                                    Country.valueOf(resultSet.getString(18)))));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map employees!", e);
        }
        return employees;
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
