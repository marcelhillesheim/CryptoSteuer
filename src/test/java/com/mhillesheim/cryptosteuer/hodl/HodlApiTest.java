package com.mhillesheim.cryptosteuer.hodl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhillesheim.cryptosteuer.transactions.entities.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.mhillesheim.cryptosteuer.transactions.Currency.BITCOIN;
import static com.mhillesheim.cryptosteuer.transactions.Currency.EURO;
import static com.mhillesheim.cryptosteuer.transactions.TradingPlatform.BINANCE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HodlApiTest {

    @Autowired
    private MockMvc mockMvc;

    // Integration test: testing, if the transactions required for the hodl algorithm are correctly retrieved from the repository
    // the transactions should be sorted by Date for the hodl algorithm
    @Test
    public void testHodlApi() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        ObjectMapper objectMapper = new ObjectMapper();

        // transactions out of order -> has to be retrieved sorted for the HodlService
        Transaction[] transactions = {
                new Transaction("b0", BINANCE, EURO, BITCOIN, EURO, BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO, dateFormat.parse("1.1.2000")),
                new Transaction("b2", BINANCE, EURO, BITCOIN, EURO, BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO, dateFormat.parse("1.3.2000")),
                new Transaction("b1", BINANCE, EURO, BITCOIN, EURO, BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO, dateFormat.parse("1.2.2000"))
        };

        for (Transaction transaction : transactions) {
            this.mockMvc.perform(
                    post("/api/v1/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    transaction
                            ))
            ).andExpect(status().isOk());
        }

        MvcResult result = this.mockMvc.perform(
                get("/api/v1/hodl/BITCOIN")
        )
                //check if HodlPeriods are correctly sorted
                //-> transaction got correctly retrieved from the repository and sorted
                .andExpect(jsonPath("$.[0].startTransaction.platformId").value("b0"))
                .andExpect(jsonPath("$.[1].startTransaction.platformId").value("b1"))
                .andExpect(jsonPath("$.[2].startTransaction.platformId").value("b2"))

                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

}
