package com.cico643.simplebanking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateBankAccountRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String owner;
    @NotNull
    @NotEmpty
    @NotBlank
    private String accountNumber;

    public CreateBankAccountRequest(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
