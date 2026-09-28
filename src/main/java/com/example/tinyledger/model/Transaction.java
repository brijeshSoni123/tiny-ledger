package com.example.tinyledger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transaction{
    private UUID id;
    private TransactionType type;
    private BigDecimal inputAmount;
    private BigDecimal currentBalance;
    private ZonedDateTime transactionTimestamp;
}
