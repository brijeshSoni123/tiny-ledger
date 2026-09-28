package com.example.tinyledger.repository;

import com.example.tinyledger.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LedgerRepository {

    // h2 db can be replaced with this easily
    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();


    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public List<Transaction> findAll() {
        synchronized (transactions) {
            return List.copyOf(transactions.values());
        }
    }

    public Optional<Transaction> getLastTransaction() {
        synchronized (transactions) {
            return transactions.values().stream().sorted(Comparator.comparing(Transaction::getTransactionTimestamp).reversed()).findFirst();
        }
    }

}
