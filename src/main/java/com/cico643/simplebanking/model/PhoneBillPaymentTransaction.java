package com.cico643.simplebanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "phone_bill_payment_transaction")
public class PhoneBillPaymentTransaction extends WithdrawalTransaction{
    @NotNull
    @NotEmpty
    private String payee;

    @Size(min = 10, max = 10)
    private String phoneNumber;

    public PhoneBillPaymentTransaction() {

    }

    public PhoneBillPaymentTransaction(BankAccount account, BigDecimal amount, LocalDateTime date, String payee, String phoneNumber) {
        super(account, amount, date);
        this.payee = payee;
        this.phoneNumber = phoneNumber;
        this.type = TransactionType.PHONE_BILL_PAYMENT_TRANSACTION;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
