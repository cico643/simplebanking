package com.cico643.simplebanking.repository;

import com.cico643.simplebanking.model.PhoneBillPaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneBillPaymentTransactionRepository extends JpaRepository<PhoneBillPaymentTransaction, String> {
}
