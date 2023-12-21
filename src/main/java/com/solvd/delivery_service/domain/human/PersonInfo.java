package com.solvd.delivery_service.domain.human;

import com.solvd.delivery_service.domain.area.Address;

import java.util.Objects;

public class PersonInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Passport passport;
    private Address address;

    public PersonInfo() {}

    public PersonInfo(String firstName, String lastName, Integer age, Passport passport, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.passport = passport;
        this.address = address;
    }

    public PersonInfo(Long id, String firstName, String lastName, Integer age, Passport passport, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.passport = passport;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonInfo that = (PersonInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(age, that.age) &&
                Objects.equals(passport, that.passport) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, passport, address);
    }

    @Override
    public String toString() {
        return "Person info: [Person id = " + id +
                ", First name = " + firstName +
                ", Last name = " + lastName +
                ", Age = " + age +
                ", \nPassport = " + passport +
                ", \nAddress = " + address + "]";
    }
}
