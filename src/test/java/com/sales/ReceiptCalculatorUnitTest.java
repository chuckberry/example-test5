package com.sales;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class ReceiptCalculatorUnitTest {

    @Mock Good mockedStandardGood, mockedExemptGood, mockedImportedGood;

    @Before
    public void before () {
        BasicConfigurator.configure();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvalidQty() {
        Good testGood = GoodFactory.getGood("wer music CD at 23");
        Assert.assertEquals("Wrong expected qty ", -1, testGood.getQty());
    }

    @Test
    public void testInvalidPrice() {
        Good testGood = GoodFactory.getGood("1 music CD at sdf");
        Assert.assertEquals("Wrong expected price ", Double.NaN, testGood.getPrice(), 0.005);
    }

    @Test
    public void testUndefinedName() {
        Good testGood = GoodFactory.getGood("1 at 23");
        Assert.assertEquals("Wrong expected name ", "<undefined>", testGood.getName());
    }

    @Test
    public void testImpossibleToGetPrice() {
        Good testGood = GoodFactory.getGood("1 music CD sdf");
        Assert.assertEquals("Wrong expected price ", Double.NaN, testGood.getPrice(), 0.005);
    }

    @Test
    public void testTaxRoundingRule () {

        Good testGood = GoodFactory.getGood("1 Imported bottle of chocolates at 11.25");
        Assert.assertEquals("Wrong expected TAX for imported good", 0.60, testGood.getTax(), 0.0005);

        testGood = GoodFactory.getGood("1 Imported chocolate at 47.50");
        Assert.assertEquals("Wrong expected TAX for exempt good", 2.40, testGood.getTax(), 0.0005);

        testGood = GoodFactory.getGood("1 bottle of perfume at 42.50");
        Assert.assertEquals("Wrong expected TAX for standard good", 4.25, testGood.getTax(), 0.0005);
    }

    @Test
    public void testStandardGood () {
        when(mockedStandardGood.getTax()).thenReturn(1.5);
        when(mockedStandardGood.getPrice()).thenReturn(14.99);
        when(mockedStandardGood.getTotal()).thenReturn(16.49);
        when(mockedStandardGood.getName()).thenReturn("music CD");
        Good testStandardGood = GoodFactory.getGood("1 music CD at 14.99");
        checkValues(mockedStandardGood, testStandardGood);
    }

    @Test
    public void testExemptGood () {
        when(mockedExemptGood.getTax()).thenReturn(new Double(0));
        when(mockedExemptGood.getPrice()).thenReturn(0.85);
        when(mockedExemptGood.getTotal()).thenReturn(0.85);
        when(mockedExemptGood.getName()).thenReturn("chocolate bar");
        Good testExemptGood = GoodFactory.getGood("1 chocolate bar at 0.85");
        checkValues(mockedExemptGood, testExemptGood);
    }

    @Test
    public void testImportedGood () {
        when(mockedImportedGood.getTax()).thenReturn(7.15);
        when(mockedImportedGood.getPrice()).thenReturn(47.50);
        when(mockedImportedGood.getTotal()).thenReturn(54.65);
        when(mockedImportedGood.getName()).thenReturn("Imported bottle of perfume");
        Good testImportedGood = GoodFactory.getGood("1 Imported bottle of perfume at 47.50");
        checkValues(mockedImportedGood, testImportedGood);
    }

    private void checkValues (Good mockedGood, Good expectedGood) {
        Assert.assertEquals("Wrong expected name for good ", mockedGood.getName(), expectedGood.getName());
        Assert.assertEquals("Wrong expected price for good ", mockedGood.getPrice(), expectedGood.getPrice(), 0.005);
        Assert.assertEquals("Wrong expected tax for good ", mockedGood.getTax(), expectedGood.getTax(), 0.005);
        Assert.assertEquals("Wrong expected total for good ", mockedGood.getTotal(), expectedGood.getTotal(), 0.005);
    }
}
