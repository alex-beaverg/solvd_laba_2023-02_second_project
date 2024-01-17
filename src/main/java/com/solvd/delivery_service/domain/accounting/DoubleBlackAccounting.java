package com.solvd.delivery_service.domain.accounting;

public class DoubleBlackAccounting extends AccountingDecorator {

    public DoubleBlackAccounting(IAccounting accounting) {
        super(accounting);
    }

    @Override
    public double increaseSalary() {
        return super.increaseSalary() + 10000;
    }
}
