package com.cico643.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {
    @Id
    @UuidGenerator
    public String approvalCode;
    public LocalDateTime date;
    public BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "account_number", nullable = false)
    @JsonBackReference
    public BankAccount account;

    @Column(name = "type", insertable = false, updatable = false)
    public String type;

    public abstract void action(BigDecimal amount);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!Objects.equals(approvalCode, that.approvalCode)) return false;
        if (!Objects.equals(date, that.date)) return false;
        if (!Objects.equals(amount, that.amount)) return false;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        int result = approvalCode != null ? approvalCode.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
