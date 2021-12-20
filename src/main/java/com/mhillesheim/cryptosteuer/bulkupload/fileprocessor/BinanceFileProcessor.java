package com.mhillesheim.cryptosteuer.bulkupload.fileprocessor;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import com.mhillesheim.cryptosteuer.transactions.Transaction;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BinanceFileProcessor extends FileProcessor {

    @Override
    public List<Transaction> processFile(MultipartFile multipartFile) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = File.createTempFile("binance", ".xlsx");
        multipartFile.transferTo(file);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0);
        //TODO generify mapping columns to constructor parameters in case column order is changed by binance
        //TODO for now order is hardcoded

        //TODO validate

        sheet.removeRow(sheet.getRow(0));
        for (Row row : sheet) {
            if (row.getCell(0) == null) break;
            if (row.getCell(0).getCellType().equals(CellType.BLANK)) continue;

            // values read from current row
            //TODO change timezone (UTC)
            LocalDateTime date = row.getCell(0).getLocalDateTimeCellValue();
            String tradingPair = String.valueOf(row.getCell(1));
            String tradingDirection = String.valueOf(row.getCell(2));
            // price is ignored as it can be inferred by total/amount
            BigDecimal amount = new BigDecimal(String.valueOf(row.getCell(4)));
            BigDecimal total = new BigDecimal(String.valueOf(row.getCell(5)));
            BigDecimal amountFee = new BigDecimal(String.valueOf(row.getCell(6)));
            Currency currencyFee = Currency.fromString(String.valueOf(row.getCell(7).getStringCellValue()));

            Currency currencyA = null;
            Currency currencyB = null;

            BigDecimal amountA = null;
            BigDecimal amountB = null;


            if (tradingDirection.equals("BUY")) {
                Pair<Currency,Currency> currencyPair = resolveTradingPair(tradingPair);
                currencyA = currencyPair.getSecond();
                currencyB = currencyPair.getFirst();
                amountA = total;
                amountB = amount;
            }
            //TODO SELL
            //TODO exceptions caused by invalid inputs
            //TODO check if transaction already exists in repository -> if already exists, dont add to transactions
            transactions.add(new Transaction(null, TradingPlatform.BINANCE,
                    currencyA, currencyB, currencyFee,
                    amountA, amountB, amountFee,
                    date
                    ));
        }


        //noinspection ResultOfMethodCallIgnored
        file.delete();
        return transactions;
    }

    @Override
    public TradingPlatform getTradingPlatform() {
        return TradingPlatform.BINANCE;
    }
}
