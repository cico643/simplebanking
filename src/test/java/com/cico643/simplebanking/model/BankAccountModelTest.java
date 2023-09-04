package com.cico643.simplebanking.model;

import com.cico643.simplebanking.Setup;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankAccountModelTest extends Setup {
    @Test
    void testBankAccountModel_whenMultiplePostActionExecuted_shouldReturnCorrectBalance() {
        BankAccount account = new BankAccount("Jim", "12345", getLocalDateTime());
        account.post(new DepositTransaction(account, new BigDecimal(1000), getLocalDateTime()));
        account.post(new WithdrawalTransaction(account, new BigDecimal(200), getLocalDateTime()));
        account.post(new PhoneBillPaymentTransaction(
                account,
                new BigDecimal("96.50"), getLocalDateTime(),
                "Vodafone",
                "5423345566")
        );
        assertEquals(new BigDecimal("703.50"), account.getBalance());
    }
}
