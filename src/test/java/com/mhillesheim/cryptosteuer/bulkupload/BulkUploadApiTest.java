package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.transactions.Currency;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class BulkUploadApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TransactionRepository transactionRepository;


    //TODO generify for future test implementations; ideally only adding new files to the resource folder
    //TODO add test files expected vs actual transactions
    @Test
    public void testBinance() throws Exception {
        String filePath = "src/test/resources/bulkupload/binance.xlsx";

        byte[] bytes = Files.readAllBytes(Path.of(filePath));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/bulk_upload")
                        .file("file", bytes)
                        .param("trading_platform","BINANCE"))
                .andExpect(status().is(200));

        System.out.println(transactionRepository.findByCurrencyA(Currency.BITCOIN).get(0));
    }
}
