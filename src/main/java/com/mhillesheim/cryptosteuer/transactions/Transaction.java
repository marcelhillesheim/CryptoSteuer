package com.mhillesheim.cryptosteuer.transactions;

import com.mhillesheim.cryptosteuer.tradingplatform.TradingPlatform;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @NotNull
    private Currency currencyA;
    @NotNull
    private Currency currencyB;
    private Currency currencyFee;

    @NotNull
    private BigDecimal amountA;
    @NotNull
    private BigDecimal amountB;
    private BigDecimal amountFee;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @NotNull
    private LocalDateTime executionDate;

    public Transaction() {}

    public Transaction(@Nullable String platformId, TradingPlatform tradingPlatform,
                       Currency currencyA, Currency currencyB, Currency currencyFee,
                       BigDecimal amountA, BigDecimal amountB, BigDecimal amountFee,
                       LocalDateTime executionDate) {
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

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
    }

}
