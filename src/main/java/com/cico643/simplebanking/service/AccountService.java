package com.cico643.simplebanking.service;

import com.cico643.simplebanking.dto.CreateBankAccountRequest;
import com.cico643.simplebanking.dto.MoneyCreditDebitRequest;
import com.cico643.simplebanking.exception.BankAccountNotFoundException;
import com.cico643.simplebanking.model.BankAccount;
import com.cico643.simplebanking.model.DepositTransaction;
import com.cico643.simplebanking.model.WithdrawalTransaction;
import com.cico643.simplebanking.repository.BankAccountRepository;
import com.cico643.simplebanking.repository.DepositTransactionRepository;
import com.cico643.simplebanking.repository.WithdrawalTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;
    private final DepositTransactionRepository depositTransactionRepository;
    private final WithdrawalTransactionRepository withdrawalTransactionRepository;
    private final Clock clock;
    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(BankAccountRepository bankAccountRepository,
                          DepositTransactionRepository depositTransactionRepository,
                          WithdrawalTransactionRepository withdrawalTransactionRepository,
                          Clock clock) {
        this.bankAccountRepository = bankAccountRepository;
        this.depositTransactionRepository = depositTransactionRepository;
        this.withdrawalTransactionRepository = withdrawalTransactionRepository;
        this.clock = clock;
    }

    public BankAccount create(CreateBankAccountRequest body) {
        BankAccount bankAccount = new BankAccount(body.getOwner(), body.getAccountNumber());
        var savedAccount = this.bankAccountRepository.save(bankAccount);
        log.info("Account with account number [" + savedAccount.getAccountNumber() + "] has been successfully created");
        return savedAccount;
    }

    public BankAccount getAccountByNumber(String accountNumber) {
        BankAccount bankAccount = this.bankAccountRepository.findById(accountNumber).orElseThrow(() ->
                        new BankAccountNotFoundException("Account could not find by given number [" + accountNumber +"]")
                );
        log.info("Fetched account with account number [" + accountNumber + "]");
        return bankAccount;
    }

    public String deposit(MoneyCreditDebitRequest body, String accountNumber) {
        BankAccount bankAccount = this.getAccountByNumber(accountNumber);
        DepositTransaction depositTransaction = new DepositTransaction(
                bankAccount,
                body.getAmount(),
                getLocalDateTimeNow()
        );
        bankAccount.post(depositTransaction);
        var savedTransaction = this.depositTransactionRepository.save(depositTransaction);
        log.info("Deposited " + body.getAmount() + " to account with number [" + accountNumber + "]");
        return savedTransaction.approvalCode;
    }

    public String withdraw(MoneyCreditDebitRequest body, String accountNumber) {
        BankAccount bankAccount = this.getAccountByNumber(accountNumber);
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(
                bankAccount,
                body.getAmount(),
                getLocalDateTimeNow()
        );
        bankAccount.post(withdrawalTransaction);
        var savedTransaction = this.withdrawalTransactionRepository.save(withdrawalTransaction);
        log.info("Withdraw " + body.getAmount() + " from account with number [" + accountNumber + "]");
        return savedTransaction.approvalCode;
    }



    private LocalDateTime getLocalDateTimeNow() {
        Instant instant = clock.instant();
        return LocalDateTime.ofInstant(instant, Clock.systemDefaultZone().getZone());
    }
}
