package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.TransactionTest;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HodlServiceTest extends TransactionTest {

    /*     b0   b1   s0   b2   s1    s2
           1|---------|0.5                      //        s0 consumed
            |-------------------|1              // b0        consumed
                1|--------------|               //        s1 consumed
                 |--------------------|0.5      // b1 and s2 consumed
                                                // nothing left
     */
    @Test
    public void testBasic() {doTest("basic");}

    /*     b0
           1|-------------------                // no sell transaction left -> still holding
     */
    @Test
    public void testStillHolding() {doTest("stillHolding");}

    /*     b0        s0
           1|---------|0.5                      //        s0 consumed
            |--------------------               // no sell transaction left -> still holding 0.5 of b0
    */
    @Test
    public void testStillHoldingCarryOver() {doTest("stillHoldingCarryOver");}

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
        HodlService hodlService = new HodlService(null);
        assertThat(hodlService.getHodlPeriods(buyTransactions, sellTransactions)).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    private List<HodlPeriod> getExpectedResults(String testName, List<Transaction> buyTransactions, List<Transaction> sellTransactions) throws IOException, CsvException {
        List<HodlPeriod> result = new ArrayList<>();

        String path = "src/test/resources/hodl/";
        String filePath = path + testName + "/" + "hodl.csv";

        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
        rows.forEach(row -> result.add(new HodlPeriod(
            buyTransactions.stream().filter(transaction -> transaction.getPlatformId().equals(row[0])).findFirst().orElse(null),
            sellTransactions.stream().filter(transaction -> transaction.getPlatformId().equals(row[1])).findFirst().orElse(null),
            new BigDecimal(row[2])
        )));

        return result;
    }

    @Override
    public String getPath() {
        return "src/test/resources/hodl/";
    }
}
