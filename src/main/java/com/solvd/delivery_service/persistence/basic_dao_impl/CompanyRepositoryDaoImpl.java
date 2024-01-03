package com.solvd.delivery_service.persistence.basic_dao_impl;

import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.CompanyRepository;
import com.solvd.delivery_service.persistence.ConnectionPool;
import com.solvd.delivery_service.persistence.DepartmentRepository;
import com.solvd.delivery_service.util.console_menu.DaoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyRepositoryDaoImpl implements CompanyRepository {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final String INSERT_COMPANY_QUERY = "INSERT INTO companies(name) values(?);";
    private static final String FIND_COMPANY_QUERY = "SELECT * FROM companies WHERE id = ?;";
    private static final String UPDATE_COMPANY_QUERY = "UPDATE companies SET name = ? WHERE id = ?;";
    private static final String DELETE_COMPANY_QUERY = "DELETE FROM companies WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM companies ORDER BY id;";
    private static final String GET_COUNT_OF_ENTRIES = "SELECT COUNT(*) AS companies_count FROM companies;";

    @Override
    public void create(Company company) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMPANY_QUERY,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                company.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create company!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Company> findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<Company> companyOptional;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_COMPANY_QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            companyOptional = Optional.of(new Company(resultSet.getLong(1), resultSet.getString(2)));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find company by id!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return companyOptional;
    }

    @Override
    public List<Company> findAll() {
        List<Company> companies;
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            companies = mapCompanies(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find all companies!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        for (Company company : companies) {
            company.setDepartments(DAO_SERVICE.getRepository(DepartmentRepository.class).findCompanyDepartments(company));
        }
        return companies;
    }

    @Override
    public void update(Company company) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY_QUERY)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setLong(2, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to rename company!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPANY_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete company by id!", e);
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
            throw new RuntimeException("Unable to get number of companies!", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return count;
    }

    private static List<Company> mapCompanies(ResultSet resultSet) {
        List<Company> companies = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Company company = new Company();
                company.setId(resultSet.getLong(1));
                company.setName(resultSet.getString(2));
                companies.add(company);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to map companies!", e);
        }
        return companies;
    }
}
