package com.solvd.delivery_service.domain.actions.entity_actions;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.actions.console_actions.ClassInfoActions;
import com.solvd.delivery_service.domain.actions.console_actions.DataInfoActions;
import com.solvd.delivery_service.domain.area.Address;
import com.solvd.delivery_service.domain.human.Passport;
import com.solvd.delivery_service.domain.human.PersonInfo;
import com.solvd.delivery_service.domain.human.customer.Customer;
import com.solvd.delivery_service.domain.pack.Package;
import com.solvd.delivery_service.service.CustomerService;
import com.solvd.delivery_service.service.PackageService;
import com.solvd.delivery_service.service.PersonInfoService;
import com.solvd.delivery_service.service.impl.CustomerServiceImpl;
import com.solvd.delivery_service.service.impl.PackageServiceImpl;
import com.solvd.delivery_service.service.impl.PersonInfoServiceImpl;
import com.solvd.delivery_service.util.console_menu.RequestMethods;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.solvd.delivery_service.util.Printers.*;

public class CustomerActions extends Actions implements IEntityActions {

    @Override
    public void showEntityEntries() {
        PRINT2LN.info("ALL CUSTOMERS:");
        for (Customer customer : new CustomerServiceImpl().retrieveAll()) {
            PRINTLN.info(String.format("CUSTOMER: %s\n\tPACKAGES:", customer));
            if (customer.getPackages().size() > 0) {
                customer.getPackages()
                        .forEach(pack -> PRINTLN.info(String.format("\t- %s, Cost:[%s BYN]",
                                pack, accounting.calculatePackageCost(pack))));
            } else {
                PRINTLN.info("\t(no packages)");
            }
        }
    }

    @Override
    public void registerEntityEntry() {
        PRINT2LN.info("REGISTERING CUSTOMER");
        Passport customerPassport = new Passport(RequestMethods.getStringValueFromConsole("passport number"));
        Address customerAddress = DataInfoActions.getAddressFromConsole();
        PersonInfo customerPersonInfo = DataInfoActions.getPersonInfoFromConsole(customerPassport, customerAddress);
        Customer customer = new Customer(customerPersonInfo);
        CustomerService customerService = new CustomerServiceImpl();
        customerService.create(customer);
        String firstName = customerPersonInfo.getFirstName();
        String lastName = customerPersonInfo.getLastName();
        PRINT2LN.info(String.format("CUSTOMER %s %s WAS REGISTERED", firstName, lastName));
    }

    @Override
    public void removeEntityEntry() {
        PRINT2LN.info("REMOVING CUSTOMER");
        CustomerService customerService = new CustomerServiceImpl();
        Customer customer = getExistingCustomer();
        Long customer_id = customer.getId();
        customerService.removeById(customer_id);
        String firstName = customer.getPersonInfo().getFirstName();
        String lastName = customer.getPersonInfo().getLastName();
        PRINT2LN.info(String.format("CUSTOMER %s %s WAS REMOVED", firstName, lastName));
    }

    @Override
    public void updateEntityEntryField() {
        PRINT2LN.info("UPDATING CUSTOMER");
        PersonInfoService personInfoService = new PersonInfoServiceImpl();
        Customer customer = getExistingCustomer();
        String oldValue = "", newValue = "";
        PersonInfo personInfo = customer.getPersonInfo();
        Field personInfoField = ClassInfoActions.getAnyClassFieldFromConsole(PersonInfo.class);
        Map<String, String> values = PackageActions.switchPersonInfoField(personInfoField, personInfo);
        oldValue = values.get("old");
        newValue = values.get("new");
        if (!personInfoField.getName().equals("passport") && !personInfoField.getName().equals("address")) {
            personInfoService.updateField(personInfo, personInfoField.getName());
        }
        String firstName = customer.getPersonInfo().getFirstName();
        String lastName = customer.getPersonInfo().getLastName();
        PRINT2LN.info(String.format("CUSTOMER %s %s FIELD %s WAS UPDATED FROM %s TO %s",
                firstName, lastName, personInfoField.getName(), oldValue, newValue));
    }

    public static void showCustomerPackages() {
        PRINT2LN.info("CUSTOMER SEARCH");
        Customer customer = getCustomerByLastName();
        String firstName = customer.getPersonInfo().getFirstName();
        String lastName = customer.getPersonInfo().getLastName();
        PRINT2LN.info(String.format("CUSTOMER %s %s PACKAGES:", firstName, lastName));
        PackageService packageService = new PackageServiceImpl();
        for (Package pack : packageService.retrieveCustomerPackages(customer)) {
            PRINTLN.info(String.format("\t- %s, Cost:[%s BYN]", pack, accounting.calculatePackageCost(pack)));
        }
    }

    protected static Customer getCustomerByLastName() {
        List<Customer> customers;
        String customerLastName;
        do {
            customerLastName = RequestMethods.getStringValueFromConsole("customer last name");
            customers = new CustomerServiceImpl().retrieveEntriesByLastName(customerLastName);
            if (customers.size() == 0) {
                PRINTLN.info(String.format("[Info]: Customer with last name %s doesn't exist!", customerLastName));
            }
        } while (customers.size() == 0);
        int index = 1;
        PRINTLN.info("Choose the customer:");
        for (Customer item : customers) {
            printAsMenu.print(index, item.getPersonInfo().getFirstName() + " " + item.getPersonInfo().getLastName());
            index++;
        }
        return customers.get(RequestMethods.getNumberFromChoice("customer number", index - 1) - 1);
    }

    private static Customer getExistingCustomer() {
        CustomerService customerService = new CustomerServiceImpl();
        List<Customer> customers = customerService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the customer:");
        for (Customer customer : customers) {
            printAsMenu.print(index, String.format("%s %s", customer.getPersonInfo().getFirstName(), customer.getPersonInfo().getLastName()));
            index++;
        }
        return customers.get(RequestMethods.getNumberFromChoice("customer number", index - 1) - 1);
    }
}
