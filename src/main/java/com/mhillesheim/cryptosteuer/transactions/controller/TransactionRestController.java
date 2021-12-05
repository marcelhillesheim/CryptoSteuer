package com.mhillesheim.cryptosteuer.transactions.controller;

import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import com.mhillesheim.cryptosteuer.transactions.repositories.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionRestController {
    private final TransactionRepository transactionRepository;

    public TransactionRestController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<Transaction> getTransactions() {
        return this.transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable("id") Optional<Transaction> transactionOptional) {
        if (!transactionOptional.isPresent() ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transactionOptional.get(), HttpStatus.OK);
    }

    @PostMapping()
    public Transaction newTransaction(@RequestBody Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }
}
