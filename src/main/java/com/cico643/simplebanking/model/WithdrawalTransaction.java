package com.cico643.simplebanking.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = TransactionType.Values.WITHDRAWAL_TRANSACTION)
public class WithdrawalTransaction extends Transaction{

    public WithdrawalTransaction() {
    }

    public WithdrawalTransaction(BankAccount account, BigDecimal amount, LocalDateTime date) {
        this.account = account;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public void action(BigDecimal amount) {
        this.account.setBalance(this.account.getBalance().subtract(amount));
    }
}
