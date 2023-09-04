package com.cico643.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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
    private LocalDateTime createDate;
    @OneToMany(mappedBy = "account", targetEntity = Transaction.class)
    @JsonManagedReference
    private Set<Transaction> transactions = new HashSet<>();

    public BankAccount() {
    }

    public BankAccount(String owner, String accountNumber, LocalDateTime createDate) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.createDate = createDate;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        if (!Objects.equals(owner, that.owner)) return false;
        if (!accountNumber.equals(that.accountNumber)) return false;
        if (!Objects.equals(balance, that.balance)) return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        return Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + accountNumber.hashCode();
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }
}
