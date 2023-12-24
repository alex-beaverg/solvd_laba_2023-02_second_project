package com.solvd.delivery_service.domain.accounting;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.employee.Employee;
import com.solvd.delivery_service.domain.pack.Package;

public class Accounting {

    public static int calculateZones(Address addressFrom, Address addressTo) {
        int x = Math.abs(addressFrom.getCountry().getZone().getIndexX() - addressTo.getCountry().getZone().getIndexX());
        int y = Math.abs(addressFrom.getCountry().getZone().getIndexY() - addressTo.getCountry().getZone().getIndexY());
        if (x + y == 0) {
            return 1;
        }
        return x + y;
    }

    public static double calculatePackageCost(Package pack) {
        double baseCost = pack.getPackageType().getBaseCost();
        double costFactor = pack.getDeliveryType().getCostFactor();
        return Math.round(baseCost * costFactor * 100.0) / 100.0;
    }

    public static double calculateEmployeeSalary(Employee employee) {
        double baseSalary = employee.getPosition().getBaseSalary();
        double salaryFactor = employee.getExperience().getSalaryFactor();
        return Math.round(baseSalary * salaryFactor * 100.0) / 100.0;
    }
}
