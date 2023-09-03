package com.cico643.simplebanking.repository;

import com.cico643.simplebanking.model.CheckTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckTransactionRepository extends JpaRepository<CheckTransaction, String> {
}
