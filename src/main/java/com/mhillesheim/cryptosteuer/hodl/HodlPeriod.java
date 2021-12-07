package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import io.micrometer.core.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class HodlPeriod {

    private final Transaction startTransaction;
    private final Transaction endTransaction;

    private final BigDecimal amount;

    public HodlPeriod(Transaction startTransaction, @Nullable Transaction endTransaction, BigDecimal amount) {
        this.startTransaction = startTransaction;
        this.endTransaction = endTransaction;
        this.amount = amount;
    }

    public LocalDateTime getStartDate() {
        return startTransaction.getExecutionDate();
    }

    public LocalDateTime getEndDate() {
            return endTransaction != null ? endTransaction.getExecutionDate() : null;
    }
    
    public final Transaction getStartTransaction() { return startTransaction; }
    
    public Transaction getEndTransaction() { return endTransaction; }
    

    public BigDecimal getAmount() {
        return amount;
    }
}
