package com.solvd.delivery_service.domain.human.customer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.pack.Package;
import jakarta.xml.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {
    @XmlAttribute(name = "id")
    private Long id;
    @JsonAlias({"person_info"})
    private PersonInfo personInfo;
    @XmlElementWrapper(name = "packages")
    @XmlElement(name = "pack")
    private List<Package> packages;

    public Customer() {}

    public Customer(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public Customer(Long id,
                    PersonInfo personInfo) {
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
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(personInfo, customer.personInfo) &&
                Objects.equals(packages, customer.packages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personInfo, packages);
    }

    @Override
    public String toString() {
        return personInfo.toString();
    }
}
