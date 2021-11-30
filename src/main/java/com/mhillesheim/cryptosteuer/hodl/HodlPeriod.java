package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("unused")
public class HodlPeriod {

    private final Transaction startTransaction;
    private final Transaction endTransaction;

    private final BigDecimal amount;

    public HodlPeriod(Transaction startTransaction, Transaction endTransaction, BigDecimal amount) {
        this.startTransaction = startTransaction;
        this.endTransaction = endTransaction;
        this.amount = amount;
    }

    public Date getStartDate() {
        return startTransaction.getExecutionDate();
    }

    public Date getEndDate() {
        return endTransaction.getExecutionDate();
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
