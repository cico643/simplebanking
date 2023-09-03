package com.cico643.simplebanking.service;

import com.cico643.simplebanking.dto.CreateBankAccountRequest;
import com.cico643.simplebanking.exception.BankAccountNotFoundException;
import com.cico643.simplebanking.model.BankAccount;
import com.cico643.simplebanking.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;
    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
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
}
