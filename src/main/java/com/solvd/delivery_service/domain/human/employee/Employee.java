package com.solvd.delivery_service.domain.human.employee;

import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.domain.structure.Department;

import java.util.List;
import java.util.Objects;

public class Employee {
    private Long id;
    private Position position;
    private Experience experience;
    private Department department;
    private PersonInfo personInfo;
    private List<Package> packages;

    public Employee() {}

    public Employee(Position position,
                    Experience experience,
                    Department department,
                    PersonInfo personInfo) {
        this.position = position;
        this.experience = experience;
        this.department = department;
        this.personInfo = personInfo;
    }

    public Employee(Long id,
                    Position position,
                    Experience experience,
                    Department department,
                    PersonInfo personInfo) {
        this.id = id;
        this.position = position;
        this.experience = experience;
        this.department = department;
        this.personInfo = personInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                position == employee.position &&
                experience == employee.experience &&
                Objects.equals(department, employee.department) &&
                Objects.equals(personInfo, employee.personInfo) &&
                Objects.equals(packages, employee.packages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, experience, department, personInfo, packages);
    }

    @Override
    public String toString() {
        return "id:[" + id + "], Pos:[" + position.getTitle() + "], Exp:[" + experience.getTitle() + "], " +
                department + ", " + personInfo;
    }
}
