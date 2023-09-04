package com.cico643.simplebanking.repository;

import com.cico643.simplebanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    public Set<Transaction> findAllByAccountAccountNumber(String accountNumber);
}
