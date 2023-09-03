package com.cico643.simplebanking.repository;

import com.cico643.simplebanking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
