package com.solvd.delivery_service.persistence.impl;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.area.Country;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.human.employee.Experience;
import com.solvd.delivery_service.domain.human.employee.Position;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.ConnectionPool;
import com.solvd.delivery_service.persistence.EmployeeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_EMPLOYEE_QUERY =
            "INSERT INTO employees(position, experience, department_id, person_id) values(?, ?, ?, ?);";
    private static final String FIND_EMPLOYEE_QUERY = "SELECT * FROM employees WHERE id = ?;";
    private static final String UPDATE_EMPLOYEE_POSITION_QUERY = "UPDATE employees SET position = ? WHERE id = ?;";
    private static final String UPDATE_EMPLOYEE_EXPERIENCE_QUERY = "UPDATE employees SET experience = ? WHERE id = ?;";
    private static final String UPDATE_EMPLOYEE_DEPARTMENT_ID_QUERY = "UPDATE employees SET department_id = ? WHERE id = ?;";
    private static final String UPDATE_EMPLOYEE_PERSON_ID_QUERY = "UPDATE employees SET person_id = ? WHERE id = ?;";
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
                            new DepartmentRepositoryImpl().findById(resultSet.getLong(3)).get(),
                            new PersonInfoRepositoryImpl().findById(resultSet.getLong(4)).get()));
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
        return employees;
    }

    @Override
    public void update(Employee employee, String field) {
        Connection connection = CONNECTION_POOL.getConnection();
        String query = null;
        String value = null;
        Class<?> clazz = String.class;
        switch (field) {
            case ("position") -> {
                query = UPDATE_EMPLOYEE_POSITION_QUERY;
                value = employee.getPosition().getTitle();
            }
            case ("experience") -> {
                query = UPDATE_EMPLOYEE_EXPERIENCE_QUERY;
                value = employee.getExperience().getTitle();
            }
            case ("department_id") -> {
                query = UPDATE_EMPLOYEE_DEPARTMENT_ID_QUERY;
                clazz = Long.class;
                value = String.valueOf(employee.getDepartment().getId());
            }
            case ("person_id") -> {
                query = UPDATE_EMPLOYEE_PERSON_ID_QUERY;
                clazz = Long.class;
                value = String.valueOf(employee.getPersonInfo().getId());
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (clazz.getSimpleName().equals(String.class.getSimpleName())) {
                preparedStatement.setString(1, value);
            } else {
                preparedStatement.setLong(1, Long.parseLong(value));
            }
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
}
