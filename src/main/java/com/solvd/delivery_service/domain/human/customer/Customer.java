package com.solvd.delivery_service.domain.human.customer;

import com.solvd.delivery_service.domain.human.PersonInfo;

import java.util.Objects;

public class Customer {
    private Long id;
    private PersonInfo personInfo;

    public Customer() {}

    public Customer(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public Customer(Long id, PersonInfo personInfo) {
        this.id = id;
        this.personInfo = personInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(personInfo, customer.personInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personInfo);
    }

    @Override
    public String toString() {
        return "Customer: [Customer id = " + id +
                ", \nPerson info = " + personInfo + "]";
    }
}
