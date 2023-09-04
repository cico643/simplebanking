package com.cico643.simplebanking.controller;

import com.cico643.simplebanking.Setup;
import com.cico643.simplebanking.dto.CreateBankAccountRequest;
import com.cico643.simplebanking.dto.MoneyCreditDebitRequest;
import com.cico643.simplebanking.model.BankAccount;
import com.cico643.simplebanking.model.DepositTransaction;
import com.cico643.simplebanking.model.WithdrawalTransaction;
import com.cico643.simplebanking.repository.BankAccountRepository;
import com.cico643.simplebanking.repository.TransactionRepository;
import com.cico643.simplebanking.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest extends Setup {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    public AccountService accountService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BankAccountRepository bankAccountRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    public final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        RestAssured.baseURI = "http://localhost:" + port;
        bankAccountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"transaction","bank_account");
    }

    @Test
    void shouldCreateNewBankAccount() {
        BankAccount bankAccount = createBankAccount();
        this.bankAccountRepository.save(bankAccount);

        CreateBankAccountRequest createBankAccountRequest = new CreateBankAccountRequest(
                "Cihat Yeşildağ",
                "12345"
        );

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(createBankAccountRequest)
                .post(ACCOUNT_ENDPOINT)
                .then()
                .statusCode(201)
                .body("data.owner", equalTo("Cihat Yeşildağ"))
                .body("success", equalTo(true))
                .body("status", equalTo("CREATED"))
                .body("message", equalTo("Account with account number [12345] has been successfully created"));
    }

    @Test
    void shouldGetAccountForGivenAccountNumber() {
        BankAccount bankAccount = createBankAccount();
        this.bankAccountRepository.save(bankAccount);

        given()
                .when()
                .get(ACCOUNT_ENDPOINT + "/" + bankAccount.getAccountNumber())
                .then()
                .statusCode(200)
                .body("data.owner", equalTo("Cihat Yeşildağ"))
                .body("success", equalTo(true))
                .body("status", equalTo("OK"))
                .body("message", equalTo("Fetched account with account number [12345]"));
    }

    @Test
    void whenAccountIsNotPresent_shouldThrowBankAccountNotFoundException() {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .pathParam("accountNumber", "12345")
                .get(ACCOUNT_ENDPOINT + "/{accountNumber}")
                .then()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("status", equalTo("NOT_FOUND"))
                .body("message", equalTo("Account could not find by given number [12345]"));
    }

    @Test
    void shouldDepositMoneyToTheAccountAndCreateRelatedTransaction() {
        MoneyCreditDebitRequest moneyCreditDebitRequest = new MoneyCreditDebitRequest(new BigDecimal(1000));
        BankAccount bankAccount = createBankAccount();
        bankAccount.setBalance(new BigDecimal(1000));

        DepositTransaction depositTransaction = new DepositTransaction(
                bankAccount,
                new BigDecimal(1000),
                getLocalDateTime()
        );

        this.transactionRepository.save(depositTransaction);

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(moneyCreditDebitRequest)
                .when()
                .pathParam("accountNumber", "12345")
                .post(ACCOUNT_ENDPOINT + "/credit/{accountNumber}")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("status", equalTo("OK"))
                .body("message", equalTo("Deposited 1000 to account with number [12345]"));
    }

    @Test
    void shouldWithdrawMoneyFromTheAccountAndCreateRelatedTransaction() {
        MoneyCreditDebitRequest moneyCreditDebitRequest = new MoneyCreditDebitRequest(new BigDecimal(20));
        BankAccount bankAccount = createBankAccount();
        bankAccount.setBalance(new BigDecimal(1000));

        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(
                bankAccount,
                new BigDecimal(20),
                getLocalDateTime()
        );

        this.transactionRepository.save(withdrawalTransaction);

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(moneyCreditDebitRequest)
                .when()
                .pathParam("accountNumber", "12345")
                .post(ACCOUNT_ENDPOINT + "/debit/{accountNumber}")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("status", equalTo("OK"));
    }

}
