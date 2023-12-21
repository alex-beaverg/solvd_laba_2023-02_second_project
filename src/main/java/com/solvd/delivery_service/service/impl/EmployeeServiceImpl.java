package com.solvd.delivery_service.service.impl;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.structure.Department;
import com.solvd.delivery_service.persistence.EmployeeRepository;
import com.solvd.delivery_service.persistence.impl.EmployeeRepositoryImpl;
import com.solvd.delivery_service.service.DepartmentService;
import com.solvd.delivery_service.service.EmployeeService;
import com.solvd.delivery_service.service.PersonInfoService;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PersonInfoService personInfoService;
    private final DepartmentService departmentService;

    public EmployeeServiceImpl() {
        this.employeeRepository = new EmployeeRepositoryImpl();
        this.personInfoService = new PersonInfoServiceImpl();
        this.departmentService = new DepartmentServiceImpl();
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
    public List<Employee> retrieveAll() {
        return employeeRepository.findAll();
    }
}
