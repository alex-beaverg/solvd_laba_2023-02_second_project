package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.EmployeeRepository;
import com.solvd.delivery_service.service.DaoService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PersonInfoService;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private static final DaoService DAO_SERVICE = DaoService.getInstance();
    private final EmployeeRepository employeeRepository;
    private final PersonInfoService personInfoService;

    public EmployeeServiceImpl() {
        this.employeeRepository = DAO_SERVICE.getEmployeeRepository();
        this.personInfoService = new PersonInfoServiceImpl();
    }

    @Override
    public Employee create(Employee employee, Long departmentId) {
        employee.setId(null);
        if (employee.getPersonInfo() != null) {
            PersonInfo personInfo = personInfoService.create(employee.getPersonInfo());
            employee.setPersonInfo(personInfo);
        }
        employeeRepository.create(employee, departmentId);
        return employee;
    }

    @Override
    public Employee retrieveById(Long id) {
        return employeeRepository.findById(id).get();
    }

    @Override
    public List<Employee> retrieveAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Long retrieveNumberOfEntries() {
        return employeeRepository.countOfEntries();
    }

    @Override
    public void updateField(Employee employee, String field) {
        employeeRepository.update(employee, field);
    }

    @Override
    public void removeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> retrieveDepartmentEmployees(Department department) {
        return employeeRepository.findDepartmentEmployees(department);
    }
}