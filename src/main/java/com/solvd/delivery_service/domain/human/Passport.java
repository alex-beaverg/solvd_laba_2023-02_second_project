package com.solvd.delivery_service.domain.human;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Passport {
    @XmlAttribute(name = "id")
    private Long id;
    private String number;

    public Passport() {}

    public Passport(String number) {
        this.number = number;
    }

    public Passport(Long id,
                    String number) {
        this.id = id;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passport passport = (Passport) o;
        return Objects.equals(id, passport.id) &&
                Objects.equals(number, passport.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return String.format("Pass:[%s]", number);
    }
}
