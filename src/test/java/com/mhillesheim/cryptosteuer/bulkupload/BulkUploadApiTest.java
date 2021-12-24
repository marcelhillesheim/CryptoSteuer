package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.TransactionTest;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.mhillesheim.cryptosteuer.transactions.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class BulkUploadApiTest extends TransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void testBinance() throws Exception { doTest(TradingPlatform.BINANCE, "basic", "xlsx"); }



    private void doTest(TradingPlatform tradingPlatform, String testName, String fileType) throws Exception {
        String filePath = getPath() + tradingPlatform.name() + "/" + testName + "." + fileType;

        byte[] bytes = Files.readAllBytes(Path.of(filePath));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/bulk_upload")
                        .file("file", bytes)
                        .param("trading_platform","BINANCE"))
                .andExpect(status().is(200));

        List<Transaction> expectedResult = getTransactionList(tradingPlatform.name(), testName);
        List<Transaction> actualResult = transactionRepository.findAll();

        assertThat(actualResult)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResult);
    }

    @Override
    public String getPath() {
        return "src/test/resources/bulkupload/";
    }
}
