package com.example.tinyledger.dto;

import com.example.tinyledger.model.Transaction;
import com.example.tinyledger.model.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class TransactionResponse extends Transaction {
    private final String message;

    public TransactionResponse(String message, UUID id, TransactionType type, BigDecimal inputAmount, BigDecimal currentBalance, ZonedDateTime transactionTimestamp) {
        super(id, type, inputAmount, currentBalance, transactionTimestamp);
        this.message = message;
    }
}
