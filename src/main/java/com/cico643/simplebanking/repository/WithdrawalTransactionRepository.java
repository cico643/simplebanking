package com.cico643.simplebanking.repository;

import com.cico643.simplebanking.model.WithdrawalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalTransactionRepository extends JpaRepository<WithdrawalTransaction, String> {
}
