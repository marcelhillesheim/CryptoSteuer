package com.mhillesheim.cryptosteuer.bulkupload.fileprocessor;

import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of the strategy pattern to handle imports of transactions from different trading platforms.
 */
public interface FileProcessor {
    List<Transaction> processFile(MultipartFile file) throws IOException;
    TradingPlatform getTradingPlatform();
}
