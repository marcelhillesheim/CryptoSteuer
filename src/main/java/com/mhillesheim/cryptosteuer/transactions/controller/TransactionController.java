package com.mhillesheim.cryptosteuer.transactions.controller;

import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import com.mhillesheim.cryptosteuer.transactions.repositories.TransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionController {

    TransactionRepository transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transaction")
    public String getTransaction(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/transaction_form";
    }

    @PostMapping("/transaction")
    public String submitTransaction(Transaction transaction, RedirectAttributes ra) {
        this.transactionRepository.save(transaction);
        ra.addAttribute("submitted", true);
        return "redirect:/transaction";
    }

    @GetMapping("/transaction_list")
    public String getTransactionList(Model model) {
        model.addAttribute("transactions", this.transactionRepository.findAll());
        return "transactions/transaction_list";
    }
}
