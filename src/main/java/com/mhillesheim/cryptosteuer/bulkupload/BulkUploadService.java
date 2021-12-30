package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.bulkupload.fileprocessor.FileProcessor;
import com.mhillesheim.cryptosteuer.bulkupload.fileprocessor.FileProcessorFactory;
import com.mhillesheim.cryptosteuer.tradingplatform.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.mhillesheim.cryptosteuer.transactions.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BulkUploadService {

    @Autowired
    private FileProcessorFactory fileProcessorFactory;

    @Autowired
    private TransactionRepository transactionRepository;

    public void process(MultipartFile file, TradingPlatform tradingPlatform) throws IOException {
        FileProcessor fileProcessor = fileProcessorFactory.findStrategy(tradingPlatform);
        List<Transaction> transactions = fileProcessor.processFile(file);

        // remove transactions, which are already stored in the db.
        // This could happen, if a transaction history includes transactions from a previous import.
        // E.g. transaction history of January 2000 and transaction history of the entire year 2000
        List<Transaction> alreadyStoredTransactions = new ArrayList<>();
        transactions.forEach(t -> {
            if (!transactionRepository.findTransaction(t).isEmpty()) {
                    alreadyStoredTransactions.add(t);
            }
        });
        transactions.removeAll(alreadyStoredTransactions);

        transactions.forEach(t -> transactionRepository.save(t));
    }
}
