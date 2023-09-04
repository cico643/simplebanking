package com.cico643.simplebanking.service;

import com.cico643.simplebanking.dto.CreateBankAccountRequest;
import com.cico643.simplebanking.dto.MoneyCreditDebitRequest;
import com.cico643.simplebanking.exception.BankAccountNotFoundException;
import com.cico643.simplebanking.model.BankAccount;
import com.cico643.simplebanking.model.DepositTransaction;
import com.cico643.simplebanking.model.WithdrawalTransaction;
import com.cico643.simplebanking.repository.BankAccountRepository;
import com.cico643.simplebanking.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final Clock clock;
    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(BankAccountRepository bankAccountRepository,
                          TransactionRepository transactionRepository,
                          Clock clock) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.clock = clock;
    }

    public BankAccount create(CreateBankAccountRequest body) {
        BankAccount bankAccount = new BankAccount(body.getOwner(), body.getAccountNumber(), getLocalDateTimeNow());
        var savedAccount = this.bankAccountRepository.save(bankAccount);
        log.info("Account with account number [" + savedAccount.getAccountNumber() + "] has been successfully created");
        return savedAccount;
    }

    public BankAccount getAccountByNumber(String accountNumber, boolean isApiCall) {
        BankAccount bankAccount = this.bankAccountRepository.findById(accountNumber)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Account could not find by given number [" + accountNumber +"]")
                );
        if (isApiCall) {
            bankAccount.setTransactions(this.transactionRepository.findAllByAccountAccountNumber(accountNumber));
        }
        log.info("Fetched account with account number [" + accountNumber + "]");
        return bankAccount;
    }

    public String deposit(MoneyCreditDebitRequest body, String accountNumber) {
        BankAccount bankAccount = this.getAccountByNumber(accountNumber, false);
        DepositTransaction depositTransaction = new DepositTransaction(
                bankAccount,
                body.getAmount(),
                getLocalDateTimeNow()
        );
        bankAccount.post(depositTransaction);
        var savedTransaction = this.transactionRepository.save(depositTransaction);
        log.info("Deposited " + body.getAmount() + " to account with number [" + accountNumber + "]");
        return savedTransaction.approvalCode;
    }

    public String withdraw(MoneyCreditDebitRequest body, String accountNumber) {
        BankAccount bankAccount = this.getAccountByNumber(accountNumber, false);
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(
                bankAccount,
                body.getAmount(),
                getLocalDateTimeNow()
        );
            bankAccount.post(withdrawalTransaction);
        var savedTransaction = this.transactionRepository.save(withdrawalTransaction);
        log.info("Withdraw " + body.getAmount() + " from account with number [" + accountNumber + "]");
        return savedTransaction.approvalCode;
    }



    private LocalDateTime getLocalDateTimeNow() {
        Instant instant = clock.instant();
        return LocalDateTime.ofInstant(instant, Clock.systemDefaultZone().getZone());
    }
}
