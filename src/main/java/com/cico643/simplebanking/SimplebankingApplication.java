package com.cico643.simplebanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class SimplebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplebankingApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
