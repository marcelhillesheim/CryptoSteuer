package com.mhillesheim.cryptosteuer.transactions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testAllData() throws Exception {
        String transaction = """
                {
                "platformId":"idTest", "tradingPlatform":"BINANCE",
                "currencyA":"EURO", "currencyB":"BITCOIN", "currencyFee":"EURO",
                "amountA":2000, "amountB":0.5, "amountFee":5.5,
                "executionDate":"2000-01-25T12:34:56"
                }
                """;
        testPosting(transaction, status().isOk());

        // check, if String from json is correctly assigned to the enum Currency
        assertFalse(transactionRepository.findByCurrencyA(Currency.EURO).isEmpty());
    }

    @Test
    public void testMinimalData() throws Exception {
        String transaction = """
                {
                "currencyA":"EURO", "currencyB":"BITCOIN",
                "amountA":2000, "amountB":0.5,
                "executionDate":"2000-01-25T12:34:56"
                }
                """;
        testPosting(transaction, status().isOk());
    }

    @Test
    public void testMissingData() throws Exception {
        String transaction = """
                {
                "currencyA":"EURO",
                "amountA":2000, "amountB":0.5,
                "executionDate":"2000-01-25T12:34:56"
                }
                """;
        testPosting(transaction, status().is4xxClientError());
    }

    @Test
    public void testNonValidEnum() throws Exception {
        String transaction = """
                {
                "currencyA":"INVALID_ENUM", "currencyB":"BITCOIN",
                "amountA":2000, "amountB":0.5,
                "executionDate":"2000-01-25T12:34:56"
                }
                """;
        testPosting(transaction, status().is4xxClientError());
    }

    private void testPosting(String transaction, ResultMatcher matcher) throws Exception {
        this.mockMvc.perform(
                post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction)
        ).andExpect(matcher);
    }

}
