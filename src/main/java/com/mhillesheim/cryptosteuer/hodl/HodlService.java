package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.mhillesheim.cryptosteuer.transactions.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class HodlService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public HodlService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * This service connects buy transactions with sell transactions via HodlPeriod instances
     * It basically determines for every coin the user owned:
     * with which transaction it was bought and with which transaction it was sold based on FIFO.
     *
     * This is necessary to determine the time period a coin has been hold before being sold.
     * And that in turn is important as in german tax law selling coins,
     * which were hold longer than 1 year, is tax free.
     *
     *  b0 |----6-months----| s0
     *     |------------1-year-----------|s1    <-- tax free for coins, which were bought with b0 and sold with s1
     *             b1 |-----9-months-----|
     *
     * @param buyTransactions sorted list by Date old->new
     *                       all transactions have to buy the same currency
     * @param sellTransactions sorted transaction list by Date old->new
     *                         all transactions have to sell the same currency
     *
     */
    public List<HodlPeriod> getHodlPeriods(List<Transaction> buyTransactions, List<Transaction> sellTransactions) {
        List<HodlPeriod> hodlPeriods = new ArrayList<>();

        BigDecimal buyAmount = BigDecimal.ZERO;
        BigDecimal sellAmount = BigDecimal.ZERO;

        HodlPeriod hodlPeriod;

        //TODO error if more sold than bought (date!)

        while (!sellTransactions.isEmpty()) {
            // store available value of the current transactions in the queue
            if (buyAmount.compareTo(BigDecimal.ZERO) == 0) buyAmount = buyTransactions.get(0).getAmountB();
            if (sellAmount.compareTo(BigDecimal.ZERO) == 0) sellAmount = sellTransactions.get(0).getAmountA();

            // "Consume" the max value possible
            BigDecimal commonAmount = new BigDecimal(buyAmount.min(sellAmount).toString());
            buyAmount = buyAmount.subtract(commonAmount);
            sellAmount = sellAmount.subtract(commonAmount);

            // connect both transaction via the HodlPeriod class and store the "consumed" value
            hodlPeriod = new HodlPeriod(buyTransactions.get(0), sellTransactions.get(0), commonAmount);
            hodlPeriods.add(hodlPeriod);

            // if there are no values left associated with a transaction -> remove transaction
            if (buyAmount.compareTo(BigDecimal.ZERO) == 0) buyTransactions.remove(0);
            if (sellAmount.compareTo(BigDecimal.ZERO) == 0) sellTransactions.remove(0);
        }

        // No more sellTransactions left -> the rest of buyTransactions is still being hold
        if (buyAmount.compareTo(BigDecimal.ZERO) != 0) {
            hodlPeriods.add(new HodlPeriod(buyTransactions.get(0), null, buyAmount));
            buyTransactions.remove(0);
        }
        for (Transaction buyTransaction : buyTransactions) {
            hodlPeriods.add(new HodlPeriod(buyTransaction, null, buyTransaction.getAmountB()));
        }

        return hodlPeriods;
    }

    public List<HodlPeriod> getHodlPeriods(Currency currency) {
        List<Transaction> buyTransactions = transactionRepository.findByCurrencyBOrderByExecutionDateAsc(currency);
        List<Transaction> sellTransactions = transactionRepository.findByCurrencyAOrderByExecutionDateAsc(currency);

        return getHodlPeriods(buyTransactions, sellTransactions);
    }
}
