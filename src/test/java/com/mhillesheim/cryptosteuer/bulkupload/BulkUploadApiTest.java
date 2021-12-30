package com.mhillesheim.cryptosteuer.bulkupload;

import com.mhillesheim.cryptosteuer.TransactionTest;
import com.mhillesheim.cryptosteuer.tradingplatform.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import com.mhillesheim.cryptosteuer.transactions.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    //TODO also check the error message, which is send back,
    // to make sure that not another error got triggered before the to be tested error is reached.

    @Test
    public void testInvalidTradingPlatform() throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of(File.createTempFile("temp", ".txt").getPath()));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/bulk_upload")
                        .file("file", bytes)
                        .param("trading_platform","UNKNOWN_PLATFORM"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testWrongFileType() throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of(File.createTempFile("temp", ".random").getPath()));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/bulk_upload")
                        .file("file", bytes)
                        .param("trading_platform","UNKNOWN_PLATFORM"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testPlatformIndependentExceptions() throws Exception {
        String folder = "exceptions";

        // Invalid date value -> random string instead of date formatted string
        doTest(folder, TradingPlatform.BINANCE, "dateTimeParseException", "xlsx", status().is(HttpStatus.BAD_REQUEST.value()));

        // Invalid data -> String instead of float value
        doTest(folder, TradingPlatform.BINANCE, "invalidCellTypeException", "xlsx", status().is(HttpStatus.BAD_REQUEST.value()));

        // Invalid TradingPair -> trading pair can't be resolved either because String doesn't make sense or unknown trading platform
        doTest(folder, TradingPlatform.BINANCE, "unknownTradingPairException", "xlsx", status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testBinance() throws Exception {
        doTest(TradingPlatform.BINANCE, "basic", "xlsx", status().isOk());
    }




    private void doTest(TradingPlatform tradingPlatform, String testName, String fileType, ResultMatcher matcher) throws Exception {
        doTest(tradingPlatform.name(), tradingPlatform, testName, fileType, matcher);
    }

    private void doTest(String folder, TradingPlatform tradingPlatform, String testName, String fileType, ResultMatcher matcher) throws Exception {
        String filePath = getPath() + folder + "/" + testName + "." + fileType;

        byte[] bytes = Files.readAllBytes(Path.of(filePath));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/bulk_upload")
                        .file("file", bytes)
                        .param("trading_platform","BINANCE"))
                .andExpect(matcher);

        List<Transaction> actualResult = transactionRepository.findAll();

        List<Transaction> expectedResult = new ArrayList<>();
        // avoids creating 'expected data' files for exceptions as those are empty anyway
        if (!folder.equals("exceptions")) expectedResult = getTransactionList(tradingPlatform.name(), testName);

        assertThat(actualResult)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResult);

        transactionRepository.deleteAll();
    }

    @Override
    public String getPath() {
        return "src/test/resources/bulkupload/";
    }
}
