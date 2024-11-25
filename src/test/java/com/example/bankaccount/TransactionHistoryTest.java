package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class TransactionHistoryTest {
    private TransactionHistory history;

    @BeforeEach
    void setUp() {
        history = new TransactionHistory();
    }

    @Test
    void testAddAndRetrieveTransactions() {
        history.addTransaction("Deposited 100.0");
        history.addTransaction("Withdrew 50.0");
        List<String> transactions = history.getTransactions();
        assertEquals(2, transactions.size());
        assertTrue(transactions.get(0).contains("Deposited 100.0"));
        assertTrue(transactions.get(1).contains("Withdrew 50.0"));
    }

    @Test
    void testFilterTransactionsByKeyword() {
        history.addTransaction("Deposited 100.0");
        history.addTransaction("Transferred 200.0 to Account A002");
        history.addTransaction("Withdrew 50.0");

        List<String> filtered = history.filterTransactionsByKeyword("Transferred");
        assertEquals(1, filtered.size());
        assertTrue(filtered.get(0).contains("Transferred 200.0 to Account A002"));
    }
}
