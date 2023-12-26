package com.solvd.delivery_service.domain.human.employee;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;

public enum Experience implements IMenu {
    JUNIOR("Junior", 1.0, 1.1),
    JUNIOR_PLUS("Junior+", 1.5, 1.5),
    MIDDLE("Middle", 2.0, 1.9),
    MIDDLE_PLUS("Middle+", 3.0, 2.2),
    SENIOR("Senior", 5.0, 2.8),
    SENIOR_PLUS("Senior+", 10.0, 3.5),
    SUPER_SENIOR("Super senior", 100.0, 5.0);

    private final String title;
    private final Double yearsTo;
    private final Double salaryFactor;

    Experience(String title, Double yearsTo, Double salaryFactor) {
        this.title = title;
        this.yearsTo = yearsTo;
        this.salaryFactor = salaryFactor;
    }

    public String getTitle() {
        return title;
    }

    public Double getYearsTo() {
        return yearsTo;
    }

    public Double getSalaryFactor() {
        return salaryFactor;
    }

    public String getName() {
        return this.name();
    }
}
