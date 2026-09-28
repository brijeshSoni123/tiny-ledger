package com.example.tinyledger.service;

import com.example.tinyledger.dto.TransactionRequest;
import com.example.tinyledger.exception.InsufficientFundException;
import com.example.tinyledger.model.Transaction;
import com.example.tinyledger.model.TransactionType;
import com.example.tinyledger.repository.LedgerRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository){
        this.ledgerRepository = ledgerRepository;
    }

    public Transaction recordTransaction(@NonNull TransactionRequest transactionRequest){
        BigDecimal existinBalance = getBalance();
        BigDecimal currentBalance;
        if(TransactionType.DEPOSIT == transactionRequest.type()){
            currentBalance = existinBalance.add(transactionRequest.amount());
        }else{
            currentBalance = existinBalance.subtract(transactionRequest.amount());
            if(currentBalance.compareTo(BigDecimal.ZERO) < 0){
                throw new InsufficientFundException("Withdrawal of '%s' exceeds current balance: '%s'".formatted(transactionRequest.amount(), existinBalance));
            }
        }
        Transaction transaction = new Transaction(UUID.randomUUID(), transactionRequest.type(), transactionRequest.amount(), currentBalance, ZonedDateTime.now());
        return ledgerRepository.save(transaction);
    }

    public BigDecimal getBalance() {
        Optional<Transaction> lastTransaction = ledgerRepository.getLastTransaction();
        return lastTransaction.isPresent() ? lastTransaction.get().getCurrentBalance() : BigDecimal.ZERO;
    }

    public List<Transaction> getHistory() {
        return ledgerRepository.findAll();
    }
}
