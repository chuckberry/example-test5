package com.sales;

public class GoodDecorator implements Good {

    private Good good;

    public GoodDecorator(Good good) {
        this.good = good;
    }

    @Override
    public String getName() {
        return good.getName();
    }

    @Override
    public int getQty() {
        return good.getQty();
    }

    @Override
    public double getTax() {
        double standardTaxRate = Settings.getInstance().getSettingsBean().getStandardTax();
        return TaxCalculator.calculate(good.getPrice(), standardTaxRate);
    }

    @Override
    public double getPrice() {
        return good.getPrice();
    }

    @Override
    public double getTotal() {
        return good.getPrice()+getTax();
    }

}
