package com.cico643.simplebanking.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = TransactionType.Values.CHECK_TRANSACTION)
public class CheckTransaction extends WithdrawalTransaction{
    private String payee;

    public CheckTransaction() {
    }

    public CheckTransaction(BankAccount account, BigDecimal amount, LocalDateTime date, String payee) {
        super(account, amount, date);
        this.payee = payee;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}
