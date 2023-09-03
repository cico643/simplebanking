package com.cico643.simplebanking.repository;

import com.cico643.simplebanking.model.DepositTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositTransactionRepository extends JpaRepository<DepositTransaction, String> {
}
