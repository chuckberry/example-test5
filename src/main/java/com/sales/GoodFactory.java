package com.sales;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GoodFactory {

    private static final Logger logger = Logger.getLogger(GoodFactory.class);
    private static final String AT_TOKEN = "at";
    private static final String UNDEFINED_GOOD = "<undefined>";
    enum GoodType {EXEMPT, IMPORTED};

    private static List<String> getTokenizedItem(String item) {
        String[] tokenizedItemArray = item.split(" ");
        if (tokenizedItemArray == null || tokenizedItemArray.length == 0) {
            logger.error("Invalid item ");
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(tokenizedItemArray));
    }

    private static int extractQuantity (List<String> tokens) {
        try {
            int quantity = Integer.parseInt(tokens.get(0));
            tokens.remove(0);
            return quantity;
        } catch (NumberFormatException ne) {
            logger.error("Invalid quantity", ne);
        }

        return -1;
    }

    private static double extractPrice (List<String> tokens) {
        try {
            double price = Double.parseDouble(tokens.get(tokens.size() - 1));
            tokens.remove(tokens.size() - 1);
            if (AT_TOKEN.equalsIgnoreCase(tokens.get(tokens.size() - 1))) {
                tokens.remove(tokens.size() - 1);
                return price;
            } else {
                logger.error("Impossible to retrieve price");
            }
        } catch (NumberFormatException ne) {
            logger.error("Invalid price", ne);
        }

        return Double.NaN;
    }

    private static String parseName (List<String> tokens) {
        if (tokens.size() == 0) {
            logger.warn("Thre is an undefined item in the receipt");
            return UNDEFINED_GOOD;
        }

        return tokens.stream().collect(Collectors.joining(" "));
    }

    private static List<GoodType> parseTypes (List<String> tokens) {

        List<String> exemptGoods = Settings.getInstance().getSettingsBean().getExempt();
        final List<GoodType> types = new ArrayList<>();
        tokens.stream().forEach(t -> {
            if (GoodType.IMPORTED.name().equalsIgnoreCase(t)) {
                types.add(GoodType.IMPORTED);
            } else {
                Optional<String> type = exemptGoods.stream().filter(e -> t.matches(e)).findFirst();
                if (type.isPresent()) {
                    types.add(GoodType.EXEMPT);
                }
            }
        });

        return types;
    }

    public static Good getGood (String item) {

        if (item == null) {
            logger.error("Invalid item");
            return null;
        }

        Good basicGood, goodDecorated;
        List<String> tokens = getTokenizedItem(item);
        int quantity = extractQuantity(tokens);
        double price = extractPrice(tokens);
        String name = parseName(tokens);
        List<GoodType> types = parseTypes(tokens);

        basicGood = new BasicGood(name, price, quantity);
        if (types.contains(GoodType.EXEMPT)) {
            goodDecorated = new ExemptGoodDecorator(basicGood);
        } else {
            goodDecorated = new GoodDecorator(basicGood);
        }

        if (types.contains(GoodType.IMPORTED)) {
            return new ImportedGoodDecorator(goodDecorated);
        } else {
            return goodDecorated;
        }
    }

}
