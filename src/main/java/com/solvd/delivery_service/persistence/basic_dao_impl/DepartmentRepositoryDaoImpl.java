package com.solvd.delivery_service.persistence.basic_dao_impl;

import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentRepositoryDaoImpl implements DepartmentRepository {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_DEPARTMENT_QUERY = "INSERT INTO departments(title) values(?);";
    private static final String FIND_DEPARTMENT_QUERY = "SELECT * FROM departments WHERE id = ?;";
    private static final String UPDATE_DEPARTMENT_QUERY = "UPDATE departments SET title = ? WHERE id = ?;";
    private static final String DELETE_DEPARTMENT_QUERY = "DELETE FROM departments WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM departments ORDER BY id;";
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS departments_count FROM departments;";

    @Override
    public void create(Department department) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DEPARTMENT_QUERY,
                Statement.RETURN_GENERATED_KEYS)) {
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
            throw new RuntimeException("Unable to find department by id!", e);
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
            throw new RuntimeException("Unable to find all departments!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        for (Department department : departments) {
            department.setEmployees(DAO_SERVICE.getRepository(EmployeeRepository.class).findDepartmentEmployees(department));
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
            throw new RuntimeException("Unable to rename department!", e);
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
            throw new RuntimeException("Unable to delete department by id!", e);
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
            throw new RuntimeException("Unable to get number of departments!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
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
            throw new RuntimeException("Unable to map departments!", e);
        }
        return departments;
    }
}