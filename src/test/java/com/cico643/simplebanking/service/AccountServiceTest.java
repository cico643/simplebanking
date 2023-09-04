package com.cico643.simplebanking.service;

import com.cico643.simplebanking.Setup;
import com.cico643.simplebanking.dto.CreateBankAccountRequest;
import com.cico643.simplebanking.dto.MoneyCreditDebitRequest;
import com.cico643.simplebanking.exception.BankAccountNotFoundException;
import com.cico643.simplebanking.model.BankAccount;
import com.cico643.simplebanking.model.DepositTransaction;
import com.cico643.simplebanking.model.WithdrawalTransaction;
import com.cico643.simplebanking.repository.BankAccountRepository;
import com.cico643.simplebanking.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountServiceTest extends Setup {
    private AccountService subject;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new AccountService(
                bankAccountRepository,
                transactionRepository,
                clock);
        when(clock.instant()).thenReturn(getCurrentInstant());
        when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());
    }

    @Test
    void create_whenOwnerAndAccountNumberProvided_shouldCreateAccount() {
        CreateBankAccountRequest createBankAccountRequest = new CreateBankAccountRequest(
                "Cihat Yeşildağ",
                "12345");

        BankAccount bankAccount = createBankAccount();
        when(this.bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount result = subject.create(createBankAccountRequest);
        assertEquals(bankAccount, result);
    }

    @Test
    void getAccountByNumber_whenGivenAccountIsPresent_shouldReturnTheAccount() {
        String accountNumber = "12345";
        BankAccount bankAccount = createBankAccount();
        when(this.bankAccountRepository.findById(accountNumber)).thenReturn(Optional.of(bankAccount));

        BankAccount result = subject.getAccountByNumber(accountNumber, false);
        assertEquals(bankAccount, result);
    }

    @Test
    void getAccountByNumber_whenGivenAccountIsNotPresent_shouldThrowError() {
        String accountNumber = "12345";

        when(this.bankAccountRepository.findById(anyString())).thenThrow(
                new BankAccountNotFoundException("Account could not find by given number [" + accountNumber +"]")
        );
        BankAccountNotFoundException thrown = assertThrows(
                BankAccountNotFoundException.class,
                () -> subject.getAccountByNumber(accountNumber, false)
        );
        assertEquals(thrown.getMessage(), "Account could not find by given number [" + accountNumber +"]");
    }

    @Test
    void deposit_whenGivenParamsAreCorrect_shouldCreateTransactionAndDepositTheMoney() throws JsonProcessingException {
        String accountNumber = "12345";
        BankAccount bankAccount = createBankAccount();

        MoneyCreditDebitRequest moneyCreditDebitRequest = new MoneyCreditDebitRequest(new BigDecimal(1000));
        when(this.bankAccountRepository.findById(accountNumber)).thenReturn(Optional.of(bankAccount));
        DepositTransaction depositTransaction = new DepositTransaction(
                bankAccount,
                moneyCreditDebitRequest.getAmount(),
                getLocalDateTime()
        );

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(depositTransaction);
        DepositTransaction savedTransaction = mapper.readValue(jsonString, DepositTransaction.class);
        String randomUUID = UUID.randomUUID().toString();
        savedTransaction.approvalCode = randomUUID;
        when(this.transactionRepository.save(depositTransaction)).thenReturn(savedTransaction);

        String result = subject.deposit(moneyCreditDebitRequest, accountNumber);

        assertEquals(randomUUID, result);
        assertEquals(new BigDecimal(1000), bankAccount.getBalance());
    }

    @Test
    void withdraw_whenGivenParamsAreCorrect_shouldCreateTransactionAndWithdrawTheMoney() throws JsonProcessingException {
        String accountNumber = "12345";
        BankAccount bankAccount = createBankAccount();

        MoneyCreditDebitRequest moneyCreditDebitRequest = new MoneyCreditDebitRequest(new BigDecimal(50));
        when(this.bankAccountRepository.findById(accountNumber)).thenReturn(Optional.of(bankAccount));
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(
                bankAccount,
                moneyCreditDebitRequest.getAmount(),
                getLocalDateTime()
        );

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(withdrawalTransaction);
        WithdrawalTransaction savedTransaction = mapper.readValue(jsonString, WithdrawalTransaction.class);
        String randomUUID = UUID.randomUUID().toString();
        bankAccount.setBalance(new BigDecimal(1000));
        savedTransaction.approvalCode = randomUUID;
        when(this.transactionRepository.save(withdrawalTransaction)).thenReturn(savedTransaction);

        String result = subject.withdraw(moneyCreditDebitRequest, accountNumber);

        assertEquals(randomUUID, result);
        assertEquals(new BigDecimal(950), bankAccount.getBalance());
    }

}
