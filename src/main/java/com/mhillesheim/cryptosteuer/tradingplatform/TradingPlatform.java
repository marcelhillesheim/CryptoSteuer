package com.mhillesheim.cryptosteuer.tradingplatform;

import java.util.Arrays;
import java.util.List;

public enum TradingPlatform {
    BITCOINDE,
    BINANCE;

    public static List<TradingPlatform> getAllTradingPlatforms() {
        return Arrays.asList(TradingPlatform.class.getEnumConstants());
    }
}
