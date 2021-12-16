package com.mhillesheim.cryptosteuer.transactions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCurrencyA(Currency currency);
    List<Transaction> findByCurrencyB(Currency currency);
}
