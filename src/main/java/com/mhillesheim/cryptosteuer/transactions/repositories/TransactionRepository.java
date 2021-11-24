package com.mhillesheim.cryptosteuer.transactions.repositories;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCurrencyA(Currency currency);
    List<Transaction> findByCurrencyB(Currency currency);
}
