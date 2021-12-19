package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.bulkupload.fileprocessor.FileProcessor;
import com.mhillesheim.cryptosteuer.bulkupload.fileprocessor.FileProcessorFactory;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.mhillesheim.cryptosteuer.transactions.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        transactions.forEach(t -> transactionRepository.save(t));
    }
}
