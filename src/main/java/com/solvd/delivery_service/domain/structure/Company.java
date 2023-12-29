package com.solvd.delivery_service.domain.structure;

import java.util.List;
import java.util.Objects;

public class Company {
    private Long id;
    private String name;
    private List<Department> departments;

    public Company() {}

    public Company(String name) {
        this.name = name;
    }

    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) &&
                Objects.equals(name, company.name) &&
                Objects.equals(departments, company.departments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departments);
    }

    @Override
    public String toString() {
        return "Company:[" + name + "]";
    }
}
