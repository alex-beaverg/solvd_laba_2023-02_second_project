package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.EmployeeRepository;
import com.solvd.delivery_service.persistence.impl.EmployeeRepositoryDaoImpl;
import com.solvd.delivery_service.service.DepartmentService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PersonInfoService;

import java.util.List;

public class EmployeeServiceDaoImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PersonInfoService personInfoService;
    private final DepartmentService departmentService;

    public EmployeeServiceDaoImpl() {
        this.employeeRepository = new EmployeeRepositoryDaoImpl();
        this.personInfoService = new PersonInfoServiceDaoImpl();
        this.departmentService = new DepartmentServiceDaoImpl();
    }

    @Override
    public Employee create(Employee employee) {
        employee.setId(null);
        if (employee.getPersonInfo() != null) {
            PersonInfo personInfo = personInfoService.create(employee.getPersonInfo());
            employee.setPersonInfo(personInfo);
        }
        if (employee.getDepartment() != null) {
            Department department = departmentService.create(employee.getDepartment());
            employee.setDepartment(department);
        }
        employeeRepository.create(employee);
        return employee;
    }

    @Override
    public Employee createWithExistDepartment(Employee employee) {
        employee.setId(null);
        if (employee.getPersonInfo() != null) {
            PersonInfo personInfo = personInfoService.create(employee.getPersonInfo());
            employee.setPersonInfo(personInfo);
        }
        employeeRepository.create(employee);
        return employee;
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
}
