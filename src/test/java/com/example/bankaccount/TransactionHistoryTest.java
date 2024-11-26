package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // Test to kill mutants related to LocalDateTime.now() and String conversion
    @Test
    public void testAddTransactionWithTimestamp() {
        // Capture the current time before adding the transaction
        LocalDateTime beforeAddTime = LocalDateTime.now();

        // Add a transaction
        String transactionDetails = "Deposit $100";
        history.addTransaction(transactionDetails);

        // Capture the transactions
        List<String> transactions = history.getTransactions();

        // Assertions
        assertEquals(1, transactions.size(), "Should have one transaction");

        // Verify the transaction string format
        String transactionEntry = transactions.get(0);
        assertTrue(transactionEntry.contains(transactionDetails), "Transaction details should be present");

        // Verify timestamp is present and valid
        String timestampPart = transactionEntry.split(" - ")[0];
        LocalDateTime transactionTime = LocalDateTime.parse(timestampPart,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Check that the transaction time is within a reasonable range (1 second before and after)
        assertTrue(transactionTime.isAfter(beforeAddTime.minusSeconds(1)),
                "Transaction time should be after or equal to before add time");
        assertTrue(transactionTime.isBefore(beforeAddTime.plusSeconds(1)),
                "Transaction time should be before or equal to after add time");
    }

    // Test to verify multiple transaction additions
    @Test
    public void testMultipleTransactionAdditions() {
        // Add multiple transactions
        history.addTransaction("Deposit $100");
        history.addTransaction("Withdrawal $50");
        history.addTransaction("Transfer $25");

        // Verify transactions
        List<String> transactions = history.getTransactions();
        assertEquals(3, transactions.size(), "Should have three transactions");

        // Verify each transaction contains expected details
        assertTrue(transactions.get(0).contains("Deposit $100"));
        assertTrue(transactions.get(1).contains("Withdrawal $50"));
        assertTrue(transactions.get(2).contains("Transfer $25"));
    }

    // Test to verify timestamp handling with different transaction types
    @Test
    public void testTransactionTimestampWithDifferentTypes() {
        // Test with various transaction types to ensure consistent formatting
        String[] transactionTypes = {
                "Deposit $100",
                "Withdrawal $50",
                "Transfer $25",
                "Fee $5",
                "Interest +$1.50"
        };

        for (String transaction : transactionTypes) {
            // Clear previous transactions to isolate each test
            history = new TransactionHistory();

            // Add transaction
            history.addTransaction(transaction);

            // Verify transaction
            List<String> transactions = history.getTransactions();
            assertEquals(1, transactions.size(), "Should have one transaction");

            String transactionEntry = transactions.get(0);

            // Verify timestamp format
            String[] parts = transactionEntry.split(" - ");
            assertEquals(2, parts.length, "Transaction should have timestamp and details");

            // Validate timestamp can be parsed
            assertDoesNotThrow(() -> {
                LocalDateTime.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }, "Timestamp should be in valid ISO_LOCAL_DATE_TIME format");

            // Verify transaction details
            assertEquals(transaction, parts[1], "Transaction details should match");
        }
    }

    // Edge case: Empty transaction
//    @Test
//    public void testAddEmptyTransaction() {
//        history.addTransaction("");
//
//        List<String> transactions = history.getTransactions();
//        assertEquals(1, transactions.size(), "Should allow empty transaction");
//
//        // Get the actual transaction
//        String transactionEntry = transactions.get(0);
//
//        // Verify timestamp is added at the beginning
//        assertTrue(transactionEntry.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+$"),
//                "Transaction should start with a valid timestamp");
//
//        // Verify the transaction ends with an empty string (no additional content)
//        assertTrue(transactionEntry.endsWith(""), "Transaction should end with an empty string");
//    }

}
