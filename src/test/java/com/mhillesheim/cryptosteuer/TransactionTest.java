package com.mhillesheim.cryptosteuer;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionTest {

    /**
     * Generating Transaction objects from a csv for testing purposes ->
     * comparing the generated objects from the csv (expected) with the objects generated by the tests (actual)
     */
    public List<Transaction> getTransactionList(String testName, String type) throws IOException, CsvException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        List<Transaction> transactionList = new ArrayList<>();

        String path = getPath();
        String filePath = path + testName + "/" + type + "Transactions.csv";

        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
        rows.forEach(row -> {
            String platformId = row[0];
            if (platformId.equals("null")) platformId = null;
            transactionList.add(new Transaction(
                    platformId, TradingPlatform.valueOf(row[1]),
                    Currency.valueOf(row[2]), Currency.valueOf(row[3]), Currency.valueOf(row[4]),
                    new BigDecimal(row[5]), new BigDecimal(row[6]), new BigDecimal(row[7]),
                    LocalDateTime.parse(row[8], formatter))
            );
        });

        return transactionList;
    }

    public abstract String getPath();

}
