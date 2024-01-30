package com.solvd.delivery_service.domain.accounting;

public class AccountingDecorator implements IAccounting {
    protected IAccounting accounting;

    public AccountingDecorator(IAccounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public double increaseSalary() {
        return this.accounting.increaseSalary();
    }
}
