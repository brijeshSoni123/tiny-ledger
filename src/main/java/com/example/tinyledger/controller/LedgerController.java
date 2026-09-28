package com.example.tinyledger.controller;

import com.example.tinyledger.dto.TransactionRequest;
import com.example.tinyledger.dto.TransactionResponse;
import com.example.tinyledger.model.Transaction;
import com.example.tinyledger.service.LedgerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/ledger/api/v1")
public class LedgerController {

    private static final String SUCCESS_MESSAGE = "Operation Completed Successfully.";

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    // Record Money Movement - available options (deposit and withdrawal)
    @PostMapping(value = "/transaction")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        Transaction transaction = ledgerService.recordTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(from(transaction));
    }

    @GetMapping("/balance")
    public ResponseEntity<TransactionResponse> getBalance() {
        TransactionResponse response = new TransactionResponse(SUCCESS_MESSAGE, UUID.randomUUID(),null, null, ledgerService.getBalance(), ZonedDateTime.now());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getHistory() {
        List<TransactionResponse> history = ledgerService.getHistory().stream()
                .map(this::from)
                .toList();
        return ResponseEntity.ok(history);
    }

    private TransactionResponse from(Transaction transaction){
        return new TransactionResponse(SUCCESS_MESSAGE, transaction.getId(), transaction.getType(), transaction.getInputAmount(), transaction.getCurrentBalance(), transaction.getTransactionTimestamp());
    }


}
