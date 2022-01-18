package com.mhillesheim.cryptosteuer.hodl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mhillesheim.cryptosteuer.transactions.Currency.BITCOIN;
import static com.mhillesheim.cryptosteuer.transactions.Currency.EURO;
import static com.mhillesheim.cryptosteuer.tradingplatform.TradingPlatform.BINANCE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class HodlApiTest {

    @Autowired
    private MockMvc mockMvc;

    // Integration test: testing, if the transactions required for the hodl algorithm are correctly retrieved from the repository
    // the transactions should be sorted by Date for the hodl algorithm
    @Test
    public void testHodlApi() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        // transactions out of order -> has to be retrieved sorted for the HodlService
        Transaction[] transactions = {
                new Transaction("second", BINANCE, EURO, BITCOIN, EURO,
                        BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO,
                        LocalDateTime.parse("01-01-2001 01:01:02", formatter)),
                new Transaction("hour", BINANCE, EURO, BITCOIN, EURO,
                        BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO,
                        LocalDateTime.parse("01-01-2001 02:01:01", formatter)),
                new Transaction("minute", BINANCE, EURO, BITCOIN, EURO,
                        BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO,
                        LocalDateTime.parse("01-01-2001 01:02:01", formatter)),
                new Transaction("month", BINANCE, EURO, BITCOIN, EURO,
                        BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO,
                        LocalDateTime.parse("01-02-2001 01:01:01", formatter)),
                new Transaction("day", BINANCE, EURO, BITCOIN, EURO,
                        BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO,
                        LocalDateTime.parse("02-01-2001 01:01:01", formatter)),
                new Transaction("year", BINANCE, EURO, BITCOIN, EURO,
                        BigDecimal.valueOf(100), BigDecimal.valueOf(1), BigDecimal.ZERO,
                        LocalDateTime.parse("01-01-2002 01:01:01", formatter))
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
                .andExpect(jsonPath("$.[0].startTransaction.platformId").value("second"))
                .andExpect(jsonPath("$.[1].startTransaction.platformId").value("minute"))
                .andExpect(jsonPath("$.[2].startTransaction.platformId").value("hour"))
                .andExpect(jsonPath("$.[3].startTransaction.platformId").value("day"))
                .andExpect(jsonPath("$.[4].startTransaction.platformId").value("month"))
                .andExpect(jsonPath("$.[5].startTransaction.platformId").value("year"))

                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

}
