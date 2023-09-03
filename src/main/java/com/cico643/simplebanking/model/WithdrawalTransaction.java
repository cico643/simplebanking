package com.cico643.simplebanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_transaction")
public class WithdrawalTransaction extends Transaction{

    public WithdrawalTransaction() {
        this.type = TransactionType.WITHDRAWAL_TRANSACTION;
    }

    public WithdrawalTransaction(BankAccount account, BigDecimal amount, LocalDateTime date) {
        this.type = TransactionType.WITHDRAWAL_TRANSACTION;
        this.account = account;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public void action(BigDecimal amount) {
        this.account.setBalance(this.account.getBalance().subtract(amount));
    }
}
