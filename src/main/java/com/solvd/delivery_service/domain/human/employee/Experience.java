package com.solvd.delivery_service.domain.human.employee;

public enum Experience {
    JUNIOR("Junior", 0.0, 1.0, 1.1),
    JUNIOR_PLUS("Junior+", 1.0, 1.5, 1.5),
    MIDDLE("Middle", 1.5, 2.0, 1.9),
    MIDDLE_PLUS("Middle+", 2.0, 3.0, 2.2),
    SENIOR("Senior", 3.0, 5.0, 2.8),
    SENIOR_PLUS("Senior+", 5.0, 10.0, 3.5),
    SUPER_SENIOR("Super senior", 10.0, 100.0, 5.0);

    private final String title;
    private final Double yearsFrom;
    private final Double yearsTo;
    private final Double salaryFactor;

    Experience(String title, Double yearsFrom, Double yearsTo, Double salaryFactor) {
        this.title = title;
        this.yearsFrom = yearsFrom;
        this.yearsTo = yearsTo;
        this.salaryFactor = salaryFactor;
    }

    public String getTitle() {
        return title;
    }

    public Double getYearsFrom() {
        return yearsFrom;
    }

    public Double getYearsTo() {
        return yearsTo;
    }

    public Double getSalaryFactor() {
        return salaryFactor;
    }
}
