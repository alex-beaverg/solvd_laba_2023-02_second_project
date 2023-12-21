package com.solvd.delivery_service.domain.accounting;

import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.pack.Package;

public class Accounting {

    public static int calculateZones(Address addressFrom, Address addressTo) {
        int x = Math.abs(addressFrom.getCountry().getZone().getIndexX() - addressTo.getCountry().getZone().getIndexX());
        int y = Math.abs(addressFrom.getCountry().getZone().getIndexY() - addressTo.getCountry().getZone().getIndexY());
        return x + y;
    }

    public static double calculatePackageCost(Package pack) {
        double baseCost = pack.getPackageType().getBaseCost();
        double costFactor = pack.getDeliveryType().getCostFactor();
        return Math.round(baseCost * costFactor * 100.0) / 100.0;
    }
}
