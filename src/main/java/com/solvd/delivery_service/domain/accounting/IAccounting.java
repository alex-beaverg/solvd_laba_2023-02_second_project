package com.solvd.delivery_service.domain.accounting;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;

public interface IAccounting {
    default int calculateZones(Address addressFrom, Address addressTo) {
        int x = Math.abs(addressFrom.getCountry().getZone().getIndexX() - addressTo.getCountry().getZone().getIndexX());
        int y = Math.abs(addressFrom.getCountry().getZone().getIndexY() - addressTo.getCountry().getZone().getIndexY());
        if (x + y == 0) {
            return 1;
        }
        return x + y;
    }
    default double calculatePackageCost(Package pack) {
        double baseCost = pack.getPackageType().getBaseCost();
        double costFactor = pack.getDeliveryType().getCostFactor();
        return Math.round(baseCost * costFactor * 100.0) / 100.0;
    }
    default double calculateEmployeeSalary(Employee employee) {
        double baseSalary = employee.getPosition().getBaseSalary();
        double salaryFactor = employee.getExperience().getSalaryFactor();
        return Math.round((baseSalary * salaryFactor + this.increaseSalary()) * 100.0) / 100.0;
    }
    double increaseSalary();
}
