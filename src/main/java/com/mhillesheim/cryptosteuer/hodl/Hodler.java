package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Hodler {
    private final List<HodlPeriod> hodlPeriods;

    public Hodler(List<Transaction> buyTransactions, List<Transaction> sellTransactions) {
        this.hodlPeriods = new ArrayList<>();

        BigDecimal buyAmount = BigDecimal.ZERO;
        BigDecimal sellAmount = BigDecimal.ZERO;

        HodlPeriod hodlPeriod = null;

        while (!sellTransactions.isEmpty()) {
            if (buyAmount.compareTo(BigDecimal.ZERO) == 0) {
                buyAmount = buyTransactions.get(0).getAmountB();
            }
            if (sellAmount.compareTo(BigDecimal.ZERO) == 0) {
                sellAmount = sellTransactions.get(0).getAmountA();
            }

            if (buyAmount.compareTo(sellAmount) == 0) {
                hodlPeriod = new HodlPeriod(buyTransactions.get(0), sellTransactions.get(0), buyAmount);

                buyTransactions.remove(0);
                sellTransactions.remove(0);

                buyAmount = BigDecimal.ZERO;
                sellAmount = BigDecimal.ZERO;
            } else if (buyAmount.compareTo(sellAmount) > 0) {
                hodlPeriod = new HodlPeriod(buyTransactions.get(0), sellTransactions.get(0), sellAmount);

                // 'consume' sell transaction
                sellTransactions.remove(0);

                buyAmount = buyAmount.subtract(sellAmount);
                sellAmount = BigDecimal.ZERO;
            } else if (sellAmount.compareTo(buyAmount) > 0) {
                hodlPeriod = new HodlPeriod(buyTransactions.get(0), sellTransactions.get(0), buyAmount);

                // 'consume' buy transaction
                buyTransactions.remove(0);

                sellAmount = sellAmount.subtract(buyAmount);
                buyAmount = BigDecimal.ZERO;
            }

            hodlPeriods.add(hodlPeriod);
        }

    }

    public List<HodlPeriod> getHodlPeriods() {
        return hodlPeriods;
    }
}
