package com.mhillesheim.cryptosteuer.transactions.entities;

import com.mhillesheim.cryptosteuer.transactions.Currency;
import com.mhillesheim.cryptosteuer.transactions.TradingPlatform;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // id assigned by the trading platform; can be null
    private String platformId;

    private TradingPlatform tradingPlatform;

    private Currency currencyA;
    private Currency currencyB;
    private Currency currencyFee;

    private BigDecimal amountA;
    private BigDecimal amountB;
    private BigDecimal amountFee;

    //TODO add hours and minute
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date executionDate;

    public Transaction() {}

    public Transaction(long id, String platformId, TradingPlatform tradingPlatform, Currency currencyA, Currency currencyB, Currency currencyFee, BigDecimal amountA, BigDecimal amountB, BigDecimal amountFee, Date executionDate) {
        this.id = id;
        this.platformId = platformId;
        this.tradingPlatform = tradingPlatform;
        this.currencyA = currencyA;
        this.currencyB = currencyB;
        this.currencyFee = currencyFee;
        this.amountA = amountA;
        this.amountB = amountB;
        this.amountFee = amountFee;
        this.executionDate = executionDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public TradingPlatform getTradingPlatform() {
        return tradingPlatform;
    }

    public void setTradingPlatform(TradingPlatform tradingPlatform) {
        this.tradingPlatform = tradingPlatform;
    }

    public Currency getCurrencyA() {
        return currencyA;
    }

    public void setCurrencyA(Currency currencyA) {
        this.currencyA = currencyA;
    }

    public Currency getCurrencyB() {
        return currencyB;
    }

    public void setCurrencyB(Currency currencyB) {
        this.currencyB = currencyB;
    }

    public Currency getCurrencyFee() {
        return currencyFee;
    }

    public void setCurrencyFee(Currency currencyFee) {
        this.currencyFee = currencyFee;
    }

    public BigDecimal getAmountA() {
        return amountA;
    }

    public void setAmountA(BigDecimal amountA) {
        this.amountA = amountA;
    }

    public BigDecimal getAmountB() {
        return amountB;
    }

    public void setAmountB(BigDecimal amountB) {
        this.amountB = amountB;
    }

    public BigDecimal getAmountFee() {
        return amountFee;
    }

    public void setAmountFee(BigDecimal amountFee) {
        this.amountFee = amountFee;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

}
