package com.solvd.delivery_service.domain.area;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Address {
    @XmlAttribute(name = "id")
    private Long id;
    private String city;
    private String street;
    private Integer house;
    private Integer flat;
    @JsonAlias({"zip_code"})
    private Integer zipCode;
    private Country country;

    public Address() {}

    public Address(String city,
                   String street,
                   Integer house,
                   Integer flat,
                   Integer zipCode,
                   Country country) {
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Address(Long id,
                   String city,
                   String street,
                   Integer house,
                   Integer flat,
                   Integer zipCode,
                   Country country) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouse() {
        return house;
    }

    public void setHouse(Integer house) {
        this.house = house;
    }

    public Integer getFlat() {
        return flat;
    }

    public void setFlat(Integer flat) {
        this.flat = flat;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) &&
                Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(house, address.house) &&
                Objects.equals(flat, address.flat) &&
                Objects.equals(zipCode, address.zipCode) &&
                country == address.country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street, house, flat, zipCode, country);
    }

    @Override
    public String toString() {
        return String.format("Address:[%d, %s, %s, %s, %d-%d]", zipCode, country.getTitle(), city, street, house, flat);
    }
}
