package com.cico643.simplebanking;

import com.cico643.simplebanking.model.BankAccount;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

public class Setup {
    public BankAccount createBankAccount() {
        return new BankAccount("Cihat Yeşildağ", "12345", getLocalDateTime());
    }

    public Instant getCurrentInstant() {
        String mockInstant = "2023-09-04T00:38:30Z";
        Clock clock = Clock.fixed(Instant.parse(mockInstant), Clock.systemDefaultZone().getZone());
        return Instant.now(clock);
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofInstant(getCurrentInstant(), Clock.systemDefaultZone().getZone());
    }
}
