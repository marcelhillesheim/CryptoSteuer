package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HodlerTest {

    @Test
    public void test() throws IOException, CsvException {
        List<Transaction> buyTransactions = getTransactionList("buy");
        List<Transaction> sellTransactions = getTransactionList("sell");

        Hodler hodler = new Hodler(buyTransactions, sellTransactions);

        hodler.getHodlPeriods().forEach(hodlPeriod -> System.out.println(hodlPeriod.getAmount()));
    }

    private List<Transaction> getTransactionList(String type) throws IOException, CsvException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        List<Transaction> transactionList = new ArrayList<>();

        String path = "src/test/resources/hodl/test1/";
        String filePath = path + type + "Transactions.csv";

        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
        rows.forEach(row -> transactionList.add(new Transaction(
        0, null, null, null, null, null, new BigDecimal(row[0]), new BigDecimal(row[0]), null, null)
        ));

        return transactionList;
    }

}
