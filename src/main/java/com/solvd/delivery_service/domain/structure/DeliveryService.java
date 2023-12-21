package com.solvd.delivery_service.domain.structure;

import com.solvd.delivery_service.service.impl.DepartmentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliveryService {
    private String title;
    private List<Department> departments = new ArrayList<>();

    public DeliveryService(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Department> getDepartments() {
        departments = new DepartmentServiceImpl().retrieveAll();
        return departments;
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryService that = (DeliveryService) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Delivery Service: [Delivery service Title = " + title + "]";
    }
}
