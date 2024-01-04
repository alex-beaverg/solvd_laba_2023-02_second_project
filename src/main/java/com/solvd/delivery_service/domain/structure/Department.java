package com.solvd.delivery_service.domain.structure;

import com.solvd.delivery_service.domain.human.employee.Employee;
import jakarta.xml.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Department {
    @XmlAttribute(name = "id")
    private Long id;
    private String title;
    private Company company;
    @XmlElementWrapper(name = "employees")
    @XmlElement(name = "employee")
    private List<Employee> employees;

    public Department() {}

    public Department(String title,
                      Company company) {
        this.title = title;
        this.company = company;
    }

    public Department(Long id,
                      String title,
                      Company company) {
        this.id = id;
        this.title = title;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(company, that.company) &&
                Objects.equals(employees, that.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, company, employees);
    }

    @Override
    public String toString() {
        return "Dep:[" + title + "], " + company;
    }
}
