package com.mhillesheim.cryptosteuer.bulkupload.fileprocessor;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of the strategy pattern to handle imports of transactions from different trading platforms.
 */
public abstract class FileProcessor {
    public abstract List<Transaction> processFile(MultipartFile file) throws IOException;
    public abstract TradingPlatform getTradingPlatform();

    /**
     * @param tradingPair String consisting of two currency names/abbreviations e.g. "BTCETH" -> BTC, ETH
     */
    public Pair<Currency, Currency> resolveTradingPair(String tradingPair) {
        String leftSide = "";
        String rightSide = tradingPair;
        for (char c : tradingPair.toCharArray()) {
            leftSide += c;
            rightSide = rightSide.substring(1);
            if (Currency.has(leftSide) && Currency.has(rightSide)) {
                return Pair.of(Currency.fromString(leftSide), Currency.fromString(rightSide));
            }
        }
        throw new IllegalArgumentException("Couldn't resolve trading pair: " + leftSide);
    }
}
