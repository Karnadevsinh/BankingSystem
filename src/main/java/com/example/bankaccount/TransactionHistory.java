package com.example.bankaccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
    private final List<String> transactions;

    public TransactionHistory() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(String transaction) {
        transactions.add(LocalDateTime.now() + " - " + transaction);
    }

    public List<String> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<String> filterTransactionsByKeyword(String keyword) {
        List<String> filtered = new ArrayList<>();
        for (String transaction : transactions) {
            if (transaction.toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(transaction);
            }
        }
        return filtered;
    }
}
