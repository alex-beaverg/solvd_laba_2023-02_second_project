package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.functional_interfaces.IPrintAsMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.solvd.delivery_service.util.Printers.*;

public class AdminActions {
    private static final Logger LOGGER = LogManager.getLogger(AdminActions.class);
    private static final IPrintAsMenu<Integer, String> printAsMenu = (index, line) -> PRINTLN.info("[" + index + "] - " + line);

    public static void showNumberOfEntriesInDb() {
        PRINT2LN.info("NUMBER OF TABLE ENTRIES IN DATABASE:");
        PRINTLN.info("Addresses table: " + new AddressServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Customers table: " + new CustomerServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Departments table: " + new DepartmentServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Employees table: " + new EmployeeServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Packages table: " + new PackageServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Passports table: " + new PassportServiceImpl().retrieveNumberOfEntries() + " entries");
        PRINTLN.info("Persons table: " + new PersonInfoServiceImpl().retrieveNumberOfEntries() + " entries");
    }

    public static void showPackages() {
        PRINT2LN.info("ALL PACKAGES:");
        for (Package pack : new PackageServiceImpl().retrieveAll()) {
            PRINTLN.info("id:[" + pack.getId() + "], N:[" + pack.getNumber() + "], PackageType:[" + pack.getPackageType().getTitle() +
                    "], DeliveryType:[" + pack.getDeliveryType().getTitle() + "], Status:[" + pack.getStatus().getTitle() + "], Condition:[" +
                    pack.getCondition().getTitle() + "], Customer:[" + pack.getCustomer().getPersonInfo().getFirstName() + " " +
                    pack.getCustomer().getPersonInfo().getLastName() + "], From:[" + pack.getAddressFrom().getCountry().getTitle() + " - " +
                    pack.getAddressFrom().getCity() + "], To:[" + pack.getAddressTo().getCountry().getTitle() + " - " +
                    pack.getAddressTo().getCity() + "], Cost:[" + Accounting.calculatePackageCost(pack) + " BYN]");
        }
    }
}
