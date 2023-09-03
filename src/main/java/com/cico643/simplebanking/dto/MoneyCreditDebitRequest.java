package com.cico643.simplebanking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class MoneyCreditDebitRequest {
    @Min(0)
    private BigDecimal amount;

    @JsonCreator
    public MoneyCreditDebitRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
