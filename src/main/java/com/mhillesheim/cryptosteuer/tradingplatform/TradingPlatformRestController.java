package com.mhillesheim.cryptosteuer.tradingplatform;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/tradingPlatform")
public class TradingPlatformRestController {

    @GetMapping
    public List<TradingPlatform> getTradingPlatforms() {
        return TradingPlatform.getAllTradingPlatforms();
    }

}

