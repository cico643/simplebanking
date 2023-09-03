package com.cico643.simplebanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_transaction")
public class CheckTransaction extends WithdrawalTransaction{
    @NotNull
    @NotEmpty
    private String payee;

    public CheckTransaction() {
    }

    public CheckTransaction(BankAccount account, BigDecimal amount, LocalDateTime date, String payee) {
        super(account, amount, date);
        this.payee = payee;
        this.type = TransactionType.CHECK_TRANSACTION;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}
