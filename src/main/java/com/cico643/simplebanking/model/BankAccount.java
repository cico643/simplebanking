package com.cico643.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bank_account")
public class BankAccount {
    @NotNull
    @NotEmpty
    private String owner;

    @Id
    @Column(unique = true)
    private String accountNumber;
    private BigDecimal balance = BigDecimal.ZERO;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createDate;

    @OneToMany(mappedBy = "account", targetEntity = Transaction.class)
    @JsonManagedReference
    private Set<Transaction> transactions = new HashSet<>();

    public BankAccount() {
    }

    public BankAccount(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
    }

    public void post(Transaction transaction) {
        transaction.action(transaction.amount);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
}
