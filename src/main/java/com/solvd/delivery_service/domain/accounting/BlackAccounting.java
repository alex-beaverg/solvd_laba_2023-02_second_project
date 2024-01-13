package com.solvd.delivery_service.domain.accounting;

public class BlackAccounting extends AccountingDecorator {

    public BlackAccounting(IAccounting accounting) {
        super(accounting);
    }

    @Override
    public double increaseSalary() {
        return super.increaseSalary() + 25000;
    }
}
