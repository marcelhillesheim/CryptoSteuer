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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        sheet.removeRow(sheet.getRow(0));
        for (Row row : sheet) {
            if (row.getCell(0) == null) break;
            if (row.getCell(0).getCellType().equals(CellType.BLANK)) continue;

            //Binance stores Date in UTC format -> changing to MEZ
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime utcDate = LocalDateTime.parse(String.valueOf(row.getCell(0)), formatter);
            LocalDateTime date = utcDate.atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.of("Europe/Berlin"))
                    .toLocalDateTime();

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

            Pair<Currency,Currency> currencyPair = resolveTradingPair(tradingPair);

            if (tradingDirection.equals("BUY")) {
                currencyA = currencyPair.getSecond();
                currencyB = currencyPair.getFirst();
                amountA = total;
                amountB = amount;
            } else if (tradingDirection.equals("SELL")) {
                currencyA = currencyPair.getFirst();
                currencyB = currencyPair.getSecond();
                amountA = amount;
                amountB = total;
            }
            //TODO exceptions caused by invalid inputs
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
