package com.solvd.delivery_service.domain.actions.entity_actions;

import com.solvd.delivery_service.domain.actions.Actions;
import com.solvd.delivery_service.domain.structure.Company;
import com.solvd.delivery_service.service.CompanyService;
import com.solvd.delivery_service.service.impl.CompanyServiceImpl;
import com.solvd.delivery_service.util.console_menu.RequestMethods;

import java.util.List;

import static com.solvd.delivery_service.util.Printers.*;

public class CompanyActions extends Actions implements IEntityActions {

    @Override
    public void showEntityEntries() {
        PRINT2LN.info("ALL COMPANIES:");
        for (Company company : new CompanyServiceImpl().retrieveAll()) {
            PRINTLN.info(String.format("%s\n\tDEPARTMENTS:", company));
            if (company.getDepartments().size() > 0) {
                company.getDepartments().forEach(department -> PRINTLN.info(String.format("\t- %s", department)));
            } else {
                PRINTLN.info("\t(no departments)");
            }
        }
    }

    @Override
    public void registerEntityEntry() {
        PRINT2LN.info("REGISTERING COMPANY");
        Company company = new Company(RequestMethods.getStringValueFromConsole("company name"));
        CompanyService companyService = new CompanyServiceImpl();
        companyService.create(company);
        PRINT2LN.info(String.format("COMPANY %s WAS REGISTERED", company.getName()));
    }

    @Override
    public void removeEntityEntry() {
        PRINT2LN.info("REMOVING COMPANY");
        CompanyService companyService = new CompanyServiceImpl();
        Company company = getExistingCompany();
        Long company_id = company.getId();
        companyService.removeById(company_id);
        PRINT2LN.info(String.format("COMPANY %s WAS REMOVED", company.getName()));
    }

    @Override
    public void updateEntityEntryField() {
        PRINT2LN.info("RENAMING COMPANY");
        CompanyService companyService = new CompanyServiceImpl();
        Company company = getExistingCompany();
        String newCompanyName = RequestMethods.getStringValueFromConsole("new company name");
        String oldCompanyName = company.getName();
        company.setName(newCompanyName);
        companyService.updateField(company);
        PRINT2LN.info(String.format("COMPANY %s WAS RENAMED TO %s", oldCompanyName, newCompanyName));
    }

    protected static Company getExistingCompany() {
        CompanyService companyService = new CompanyServiceImpl();
        List<Company> companies = companyService.retrieveAll();
        int index = 1;
        PRINTLN.info("Choose the company:");
        for (Company item : companies) {
            printAsMenu.print(index, item.getName());
            index++;
        }
        return companies.get(RequestMethods.getNumberFromChoice("company number", index - 1) - 1);
    }
}
