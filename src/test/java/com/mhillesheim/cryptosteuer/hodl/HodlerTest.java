package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HodlerTest  {

    /*     b0   b1   s0   b2   s1    s2
           1|---------|0.5                      //        s0 consumed
            |-------------------|1              // b0        consumed
                1|--------------|               //        s1 consumed
                 |--------------------|0.5      // b1 and s2 consumed
                           1|-------------------// b2        consumed (still holding)
     */
    @Test
    public void testBasic1() {doTest("basic1");}

    //TODO test buyAmount rest -> no sellTransactions left
    //TODO error if more sold than bought (date!)




    public void doTest(String testName)  {
        List<Transaction> buyTransactions = null;
        List<Transaction> sellTransactions = null;
        List<HodlPeriod> expectedResult = null;
        try {
            buyTransactions = getTransactionList(testName, "buy");
            sellTransactions = getTransactionList(testName, "sell");
            expectedResult = getExpectedResults(testName, buyTransactions, sellTransactions);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }


        assert sellTransactions != null;
        Hodler hodler = new Hodler(buyTransactions, sellTransactions);
        hodler.getHodlPeriods().forEach(hodlPeriod -> System.out.println(hodlPeriod.getAmount()));
        assertThat(hodler.getHodlPeriods()).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    private List<Transaction> getTransactionList(String testName, String type) throws IOException, CsvException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        List<Transaction> transactionList = new ArrayList<>();

        String path = "src/test/resources/hodl/";
        String filePath = path + testName + "/" + type + "Transactions.csv";

        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
        rows.forEach(row -> {
            try {
                transactionList.add(new Transaction(
                        row[0], TradingPlatform.valueOf(row[1]),
                        Currency.valueOf(row[2]), Currency.valueOf(row[3]), Currency.valueOf(row[4]),
                        new BigDecimal(row[5]), new BigDecimal(row[6]), new BigDecimal(row[7]),
                        dateFormat.parse(row[8]))
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return transactionList;
    }


    private List<HodlPeriod> getExpectedResults(String testName, List<Transaction> buyTransactions, List<Transaction> sellTransactions) throws IOException, CsvException {
        List<HodlPeriod> result = new ArrayList<>();

        String path = "src/test/resources/hodl/";
        String filePath = path + testName + "/" + "hodl.csv";

        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
        rows.forEach(row -> {
            result.add(new HodlPeriod(
                buyTransactions.stream().filter(transaction -> transaction.getPlatformId().equals(row[0])).findFirst().orElse(null),
                sellTransactions.stream().filter(transaction -> transaction.getPlatformId().equals(row[1])).findFirst().orElse(null),
                new BigDecimal(row[2])
            ));
        });

        return result;
    }

}