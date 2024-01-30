package com.solvd.delivery_service.domain.pack;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.human.employee.Employee;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlRootElement(name = "package")
@XmlAccessorType(XmlAccessType.FIELD)
public class Package {
    @XmlAttribute(name = "id")
    private Long id;
    @XmlElement(name = "packageNumber")
    private Long number;
    @JsonAlias({"package_type"})
    private PackageType packageType;
    @JsonAlias({"delivery_type"})
    private DeliveryType deliveryType;
    private Status status;
    private Condition condition;
    @JsonAlias({"address_from"})
    private Address addressFrom;
    @JsonAlias({"address_to"})
    private Address addressTo;
    private Customer customer;
    private Employee employee;

    public Package() {}

    public Package(Long number,
                   PackageType packageType,
                   DeliveryType deliveryType,
                   Status status,
                   Condition condition,
                   Address addressFrom,
                   Address addressTo,
                   Customer customer,
                   Employee employee) {
        this.number = number;
        this.packageType = packageType;
        this.deliveryType = deliveryType;
        this.status = status;
        this.condition = condition;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.customer = customer;
        this.employee = employee;
    }

    public Package(Long id,
                   Long number,
                   PackageType packageType,
                   DeliveryType deliveryType,
                   Status status,
                   Condition condition,
                   Address addressFrom,
                   Address addressTo,
                   Customer customer,
                   Employee employee) {
        this.id = id;
        this.number = number;
        this.packageType = packageType;
        this.deliveryType = deliveryType;
        this.status = status;
        this.condition = condition;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.customer = customer;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Address getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(Address addressFrom) {
        this.addressFrom = addressFrom;
    }

    public Address getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(Address addressTo) {
        this.addressTo = addressTo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return Objects.equals(id, aPackage.id) &&
                Objects.equals(number, aPackage.number) &&
                packageType == aPackage.packageType &&
                deliveryType == aPackage.deliveryType &&
                status == aPackage.status &&
                condition == aPackage.condition &&
                Objects.equals(addressFrom, aPackage.addressFrom) &&
                Objects.equals(addressTo, aPackage.addressTo) &&
                Objects.equals(customer, aPackage.customer) &&
                Objects.equals(employee, aPackage.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, packageType, deliveryType, status, condition, addressFrom, addressTo,
                customer, employee);
    }

    @Override
    public String toString() {
        String from = String.format("%s/%s", addressFrom.getCountry().getTitle(), addressFrom.getCity());
        String to = String.format("%s/%s", addressTo.getCountry().getTitle(), addressTo.getCity());
        String customerName = String.format("%s %s", customer.getPersonInfo().getFirstName(), customer.getPersonInfo().getLastName());
        String employeeName = String.format("%s %s", employee.getPersonInfo().getFirstName(), employee.getPersonInfo().getLastName());
        return String.format("N:[%d], P.type:[%s], D.type:[%s], Status:[%s], Cond:[%s], From:[%s], To:[%s], Cust:[%s], Empl:[%s]",
                number, packageType, deliveryType, status, condition, from, to, customerName, employeeName);
    }
}
