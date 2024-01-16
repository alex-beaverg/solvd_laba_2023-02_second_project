package com.solvd.delivery_service.domain.actions;

import com.solvd.delivery_service.domain.accounting.BlackAccounting;
import com.solvd.delivery_service.domain.accounting.IAccounting;
import com.solvd.delivery_service.domain.accounting.Accounting;
import com.solvd.delivery_service.service.impl.*;
import com.solvd.delivery_service.util.functional_interfaces.IPrintAsMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.solvd.delivery_service.util.Printers.*;

public class Actions {
    protected static final Logger LOGGER = LogManager.getLogger(Actions.class);
    protected static final IPrintAsMenu<Integer, String> printAsMenu = (index, line) -> PRINTLN.info("[" + index + "]: " + line);
    protected static IAccounting accounting = new BlackAccounting(new Accounting());

    public static void showNumberOfDatabaseEntries() {
        PRINT2LN.info("NUMBER OF TABLE ENTRIES IN DATABASE:");
        String template = "%-18s %3d entries";
        PRINTLN.info(String.format(template, "Addresses table:", new AddressServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Customers table:", new CustomerServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Companies table:", new CompanyServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Departments table:", new DepartmentServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Employees table:", new EmployeeServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Packages table:", new PackageServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Passports table:", new PassportServiceImpl().retrieveNumberOfEntries()));
        PRINTLN.info(String.format(template, "Persons table:", new PersonInfoServiceImpl().retrieveNumberOfEntries()));
    }
}