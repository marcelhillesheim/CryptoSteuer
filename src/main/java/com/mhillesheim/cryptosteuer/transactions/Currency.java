package com.mhillesheim.cryptosteuer.transactions;

import java.util.EnumSet;

public enum Currency {
    EURO,
    BITCOIN,
    ETHEREUM;

    public static final EnumSet<Currency> FIAT_CURRENCY = EnumSet.of(EURO);
}

