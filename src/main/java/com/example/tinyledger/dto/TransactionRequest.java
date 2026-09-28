package com.example.tinyledger.dto;

import com.example.tinyledger.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequest(
    @NotNull(message = "type is required and must be DEPOSIT or WITHDRAWAL")
    TransactionType type,

    @NotNull(message = "currentBalance is required")
    @DecimalMin(value = "0.01", message = "currentBalance must be greater than zero")
    BigDecimal amount){
}
