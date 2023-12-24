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
import com.solvd.delivery_service.persistence.DepartmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentRepositoryDaoImpl implements DepartmentRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_DEPARTMENT_QUERY = "INSERT INTO departments(title) values(?);";
    private static final String FIND_DEPARTMENT_QUERY = "SELECT * FROM departments WHERE id = ?;";
    private static final String UPDATE_DEPARTMENT_QUERY = "UPDATE departments SET title = ? WHERE id = ?;";
    private static final String DELETE_DEPARTMENT_QUERY = "DELETE FROM departments WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM departments ORDER BY id;";
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS departments_count FROM departments;";
    private static final String FIND_DEPARTMENT_EMPLOYEES_QUERY =
            "SELECT e.id, e.position, e.experience, " +
                    "d.id AS department_id, d.title AS department, " +
                    "p.id AS person_id, p.first_name, p.last_name, p.age, " +
                    "ps.id AS passport_id, ps.number AS passport, " +
                    "a.id AS address_id, a.city, a.street, a.house, a.flat, a.zip_code, a.country " +
            "FROM employees e " +
            "JOIN departments d ON e.department_id = d.id " +
            "JOIN persons p ON e.person_id = p.id " +
            "JOIN addresses a ON p.address_id = a.id " +
            "JOIN passports ps ON p.passport_id = ps.id " +
            "WHERE department_id = ?;";

    @Override
    public void create(Department department) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DEPARTMENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, department.getTitle());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                department.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create department!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Department> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Department> departmentOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_DEPARTMENT_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            departmentOptional = Optional.of(new Department(resultSet.getLong(1), resultSet.getString(2)));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find department!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return departmentOptional;
    }

    @Override
    public List<Department> findAll() {
        List<Department> departments;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            departments = mapDepartments(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all department!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        List<Employee> employees;
        connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_DEPARTMENT_EMPLOYEES_QUERY)) {
            int index = 1;
            for (Department department : departments) {
                preparedStatement.setLong(1, index);
                ResultSet resultSet = preparedStatement.executeQuery();
                employees = mapEmployees(resultSet);
                department.setEmployees(employees);
                index++;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find department employees!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return departments;
    }

    @Override
    public void update(Department department) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DEPARTMENT_QUERY)) {
            preparedStatement.setString(1, department.getTitle());
            preparedStatement.setLong(2, department.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update department!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DEPARTMENT_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete department!", e);
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
            throw new RuntimeException("Unable to get count of departments!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
    }

    @Override
    public List<Employee> findDepartmentEmployees(Department department) {
        List<Employee> employees;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_DEPARTMENT_EMPLOYEES_QUERY)) {
            preparedStatement.setLong(1, department.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            employees = mapEmployees(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find department employees!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return employees;
    }

    private static List<Department> mapDepartments(ResultSet resultSet) {
        List<Department> departments = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong(1));
                department.setTitle(resultSet.getString(2));
                departments.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map department!", e);
        }
        return departments;
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
                                resultSet.getString(5)));
                employee.setPersonInfo(
                        new PersonInfo(
                                resultSet.getLong(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getInt(9),
                                new Passport(
                                        resultSet.getLong(10),
                                        resultSet.getString(11)),
                                new Address(
                                        resultSet.getLong(12),
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
