package com.sales;

public class ImportedGoodDecorator implements Good {

    private Good good;

    public ImportedGoodDecorator(Good good) {
        this.good = good;
    }

    public String getName() {
        return good.getName();
    }

    @Override
    public int getQty() {
        return good.getQty();
    }

    public double getTax() {
        double importedTaxRate = Settings.getInstance().getSettingsBean().getImportedTax();
        return TaxCalculator.calculate(good.getPrice(), importedTaxRate) + good.getTax();
    }

    @Override
    public double getPrice() {
        return good.getPrice();
    }

    @Override
    public double getTotal() {
        return good.getPrice() + getTax();
    }
}
