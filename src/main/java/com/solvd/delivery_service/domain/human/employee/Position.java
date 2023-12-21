package com.solvd.delivery_service.domain.human.employee;

public enum Position {
    MANAGER_2CAT("Manager 2 category", 1495.2),
    MANAGER_1CAT("Manager 1 category", 1753.4),
    ENGINEER_2CAT("Engineer 2 category", 1615.3),
    ENGINEER_1CAT("Engineer 1 category", 1905.5);

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
}
