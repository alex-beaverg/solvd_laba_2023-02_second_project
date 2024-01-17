package com.solvd.delivery_service.domain.human.employee;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;

public enum Position implements IMenu {
    MANAGER_2CAT("Manager 2cat", 1495.2),
    MANAGER_1CAT("Manager 1cat", 1753.4),
    ENGINEER_2CAT("Engineer 2cat", 1615.3),
    ENGINEER_1CAT("Engineer 1cat", 1905.5);

    private final String title;
    private final Double baseSalary;

    Position(String title, Double baseSalary) {
        this.title = title;
        this.baseSalary = baseSalary;
    }

    public String getTitle() {
        return title;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public String getName() {
        return this.name();
    }
}
