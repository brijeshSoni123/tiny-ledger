package com.example.tinyledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinyLedgerApplication {


    // Assuming all transaction happens in GBP
    public static void main(String[] args) {
        SpringApplication.run(TinyLedgerApplication.class, args);
    }

}
