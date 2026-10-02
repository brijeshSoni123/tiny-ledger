package com.example.tinyledger.controller;

import com.example.tinyledger.dto.TransactionRequest;
import com.example.tinyledger.model.TransactionType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LedgerControllerTest {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void depositThenBalanceReflectsIt() throws Exception {
        TransactionRequest deposit = new TransactionRequest(TransactionType.DEPOSIT, new BigDecimal("150.00"));

        ResultActions resultActions = mockMvc.perform(post("/ledger/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Matchers.matchesPattern(UUID_REGEX)))
                .andExpect(jsonPath("$.currentBalance").value(150.00))
                .andExpect(jsonPath("$.transactionTimestamp").exists());

        mockMvc.perform(get("/ledger/api/v1/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentBalance").value(150.00));

        resultActions = mockMvc.perform(get("/ledger/api/v1/transactions"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].currentBalance").value(150.00));
    }

    @Test
    @Order(2)
    void withdrawalExceedingBalanceReturns422() throws Exception {
        TransactionRequest withdrawal = new TransactionRequest(TransactionType.WITHDRAWAL, new BigDecimal("9999.00"));

        final ResultActions resultActions = mockMvc.perform(post("/ledger/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawal)));

        resultActions.andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Withdrawal of '9999.00' exceeds current balance: '150.00'"));
    }

    @Test
    @Order(3)
    void negativeAmountIsRejectedWithValidationError() throws Exception {
        TransactionRequest invalid = new TransactionRequest(TransactionType.DEPOSIT, new BigDecimal("-10.00"));

        final ResultActions resultActions = mockMvc.perform(post("/ledger/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid User Input for field(s): amount"));
    }

    @Test
    @Order(4)
    void missingTypeIsRejectedWithValidationError() throws Exception {
        String badJson = "{\"amount\": 10.00}";

        final ResultActions resultActions = mockMvc.perform(post("/ledger/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void historyEndpointReturnsRecordedTransactions() throws Exception {
        TransactionRequest deposit = new TransactionRequest(TransactionType.DEPOSIT, new BigDecimal("20.00"));

        mockMvc.perform(post("/ledger/api/v1/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/ledger/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
