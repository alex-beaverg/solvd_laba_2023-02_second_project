package com.solvd.delivery_service.util.console_menu;

import com.solvd.delivery_service.domain.actions.entity_actions.*;

public class EntityActionsService {
    private static EntityActionsService instance;
    private int numberOfEntityActionsService;

    private EntityActionsService() {}

    public static EntityActionsService getInstance() {
        if (instance == null) {
            instance = new EntityActionsService();
        }
        return instance;
    }

    protected void assignEntry(String title) {
        numberOfEntityActionsService = Entity.valueOf(title).getNumber();
    }

    public IEntityActions getEntityActions() {
        IEntityActions entityActions = null;
        switch (numberOfEntityActionsService) {
            case (1) -> entityActions = new CompanyActions();
            case (2) -> entityActions = new DepartmentActions();
            case (3) -> entityActions = new EmployeeActions();
            case (4) -> entityActions = new CustomerActions();
            case (5) -> entityActions = new PackageActions();
        }
        return entityActions;
    }
}
