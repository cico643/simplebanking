package com.cico643.simplebanking.controller;

import com.cico643.simplebanking.dto.CreateBankAccountRequest;
import com.cico643.simplebanking.dto.DepositMoneyRequest;
import com.cico643.simplebanking.dto.GenericApiResponse;
import com.cico643.simplebanking.model.BankAccount;
import com.cico643.simplebanking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/v1")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<GenericApiResponse<BankAccount>> create(@Valid @RequestBody CreateBankAccountRequest body) {
        BankAccount savedAccount = this.accountService.create(body);
        GenericApiResponse<BankAccount> genericApiResponse = new GenericApiResponse<>(
            true,
            HttpStatus.CREATED,
            "Account with account number [" + savedAccount.getAccountNumber() + "] has been successfully created",
            savedAccount
        );
        return new ResponseEntity<>(genericApiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<GenericApiResponse<BankAccount>> getAccountByNumber(@PathVariable String accountNumber) {
        BankAccount account = this.accountService.getAccountByNumber(accountNumber);
        GenericApiResponse<BankAccount> genericApiResponse = new GenericApiResponse<>(
            true,
            HttpStatus.OK,
            "Fetched account with account number [" + account.getAccountNumber() + "]",
            account
        );
        return new ResponseEntity<>(genericApiResponse, HttpStatus.OK);
    }

    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<GenericApiResponse<String>> deposit(@PathVariable String accountNumber,
                                                                   @Valid @RequestBody DepositMoneyRequest body) {
        String approvalCode = this.accountService.deposit(body, accountNumber);
        GenericApiResponse<String> genericApiResponse = new GenericApiResponse<>(
            true,
            HttpStatus.OK,
            "Deposited " + body.getAmount() + " to account with number [" + accountNumber + "]",
            approvalCode
        );
        return new ResponseEntity<>(genericApiResponse, HttpStatus.OK);
    }
}
