package com.cico643.simplebanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposit_transaction")
public class DepositTransaction extends Transaction {

    public DepositTransaction() {
        this.type = TransactionType.DEPOSIT_TRANSACTION;
    }

    public DepositTransaction(BankAccount account, BigDecimal amount, LocalDateTime date) {
        this.type = TransactionType.DEPOSIT_TRANSACTION;
        this.account = account;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public void action(BigDecimal amount) {
        this.account.setBalance(this.account.getBalance().add(amount));
    }
}
