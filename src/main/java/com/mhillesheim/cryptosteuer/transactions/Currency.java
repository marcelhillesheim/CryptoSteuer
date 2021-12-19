package com.mhillesheim.cryptosteuer.transactions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Currency {
    EURO("EUR"),
    BITCOIN("BTC"),
    ETHEREUM("ETH"),
    BINANCE_COIN("BNB");

    public static final EnumSet<Currency> FIAT_CURRENCY = EnumSet.of(EURO);


    private final String abbreviation;
    static final private Map<String, Currency> ABBREVIATION_MAP = new HashMap<>();
    static {
        for (Currency currency : Currency.values()) {
            ABBREVIATION_MAP.put(currency.name().toUpperCase(), currency);
            ABBREVIATION_MAP.put(currency.abbreviation, currency);
        }
    }
    Currency(String s) {
        this.abbreviation = s;
    }



    static public boolean has(String value) {
        return ABBREVIATION_MAP.containsKey(value.toUpperCase());
    }

    static public Currency fromString(String s) {
        Currency currency = ABBREVIATION_MAP.get(s.toUpperCase());
        if (currency == null)
            throw new IllegalArgumentException("Not an alias: "+ s);
        return currency;
    }
}

