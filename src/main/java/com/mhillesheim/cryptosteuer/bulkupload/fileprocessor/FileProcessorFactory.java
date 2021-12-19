package com.mhillesheim.cryptosteuer.bulkupload.fileprocessor;

import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class FileProcessorFactory {
    private Map<TradingPlatform, FileProcessor> strategies;

    @Autowired
    public FileProcessorFactory(Set<FileProcessor> strategySet) {
        createStrategy(strategySet);
    }

    public FileProcessor findStrategy(TradingPlatform strategyName) {
        return strategies.get(strategyName);
    }
    private void createStrategy(Set<FileProcessor> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(strategy ->strategies.put(strategy.getTradingPlatform(), strategy));
    }
}
