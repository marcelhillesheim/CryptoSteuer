package com.mhillesheim.cryptosteuer.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCurrencyA(Currency currency);
    List<Transaction> findByCurrencyB(Currency currency);

    @Query(
            "SELECT t FROM Transaction t where " +
                    "t.tradingPlatform = :#{#transaction.tradingPlatform} AND " +
                    //TODO doesn't seem to work -> maybe because no values got assigned
                    // Maybe this query + OR + tradingplatform and platformid
                    //"t.platformId = :#{#transaction.platformId} AND " +
                    "t.currencyA = :#{#transaction.currencyA} AND " +
                    "t.currencyB = :#{#transaction.currencyB} AND " +
                    "t.currencyFee = :#{#transaction.currencyFee} AND " +
                    "t.amountA = :#{#transaction.amountA} AND " +
                    "t.amountB = :#{#transaction.amountB} AND " +
                    "t.amountFee = :#{#transaction.amountFee} AND " +
                    "t.executionDate = :#{#transaction.executionDate}"
    )
    List<Transaction> findTransaction(@Param("transaction") Transaction transaction);

}
