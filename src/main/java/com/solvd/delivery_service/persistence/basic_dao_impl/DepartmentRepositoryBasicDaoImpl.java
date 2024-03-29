package com.solvd.delivery_service.persistence.basic_dao_impl;

import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.*;
import com.solvd.delivery_service.util.console_menu.DaoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentRepositoryBasicDaoImpl implements DepartmentRepository {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_DEPARTMENT_QUERY = "INSERT INTO departments(title, company_id) values(?, ?);";
    private static final String UPDATE_DEPARTMENT_QUERY = "UPDATE departments SET title = ? WHERE id = ?;";
    private static final String DELETE_DEPARTMENT_QUERY = "DELETE FROM departments WHERE id = ?;";
    private static final String MAIN_QUERY =
            "SELECT d.id, d.title, c.id AS company_id, c.name " +
            "FROM departments d " +
            "JOIN companies c ON d.company_id = c.id ";
    private static final String FIND_ALL_QUERY = MAIN_QUERY + "ORDER BY d.id;";
    private static final String FIND_DEPARTMENT_QUERY = MAIN_QUERY + "WHERE d.id = ?;";
    private static final String GET_COUNT_OF_ENTRIES_QUERY = "SELECT COUNT(*) AS departments_count FROM departments;";
    private static final String FIND_COMPANY_DEPARTMENTS_QUERY = MAIN_QUERY + "WHERE c.id = ?;";

    @Override
    public void create(Department department) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DEPARTMENT_QUERY,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, department.getTitle());
            preparedStatement.setLong(2, department.getCompany().getId());
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
            departmentOptional = Optional.of(new Department(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    new Company(resultSet.getLong(3),
                            resultSet.getString(4))));
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_OF_ENTRIES_QUERY)) {
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

    @Override
    public List<Department> findCompanyDepartments(Company company) {
        List<Department> departments;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_COMPANY_DEPARTMENTS_QUERY)) {
            preparedStatement.setLong(1, company.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            departments = mapDepartments(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find company departments!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return departments;
    }

    private static List<Department> mapDepartments(ResultSet resultSet) {
        List<Department> departments = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong(1));
                department.setTitle(resultSet.getString(2));
                department.setCompany(new Company(resultSet.getLong(3), resultSet.getString(4)));
                departments.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map departments!", e);
        }
        return departments;
    }
}