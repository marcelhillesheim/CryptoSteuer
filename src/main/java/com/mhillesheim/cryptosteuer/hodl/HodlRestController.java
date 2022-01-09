package com.mhillesheim.cryptosteuer.hodl;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/hodl")
public class HodlRestController {
    private final HodlService hodlService;

    @Autowired
    public HodlRestController(HodlService hodlService) {
        this.hodlService = hodlService;
    }

    @GetMapping("/{currency}")
    public List<HodlPeriod> getHodlPeriods(@PathVariable("currency") String currency) {
        return hodlService.getHodlPeriods(Currency.valueOf(currency));
    }
}
