package com.sales;

public class TaxCalculator {

    private static final double STEP = 0.05;

    public static double calculate (double price, double taxRate) {
        double tax = price*taxRate;
        double bound = Math.round(tax * 20.0) / 20.0;
        if (tax > bound) {
            // bound is lowest nearest 0.05
            return bound + STEP;
        }
        // bound is already the nearest up 0.05
        return bound;
    }

}
