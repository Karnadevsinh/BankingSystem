package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BankAccountTest {
    @Test
    public void testDeposit_PositiveBoundaryAmount() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.deposit(0.01, "1234");

        assertTrue(result);
        assertEquals(100.01, account.getBalance(), 0.001);
    }

    @Test
    public void testDeposit_ZeroAmount() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.deposit(0.0, "1234");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDeposit_NegativeAmount() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.deposit(-1.0, "1234");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDeposit_InvalidPin() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.deposit(50.0, "wrong");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw_PositiveBoundaryAmount() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.withdraw(0.01, "1234");

        assertTrue(result);
        assertEquals(99.99, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw_ZeroAmount() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.withdraw(0.0, "1234");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw_NegativeAmount() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.withdraw(-1.0, "1234");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw_InsufficientBalance() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.withdraw(100.01, "1234");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw_InvalidPin() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        boolean result = account.withdraw(50.0, "wrong");

        assertFalse(result);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw_BoundaryConditions() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        // Test withdrawal of exactly the balance amount
        boolean result = account.withdraw(100.0, "1234");

        assertTrue(result);
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    public void testConvertBalance_MultiplicationMutant() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        account.convertBalance(1.5, "EUR");

        assertEquals(150.0, account.getBalance(), 0.001);

        // Verify transaction log
        assertTrue(account.getTransactionHistory().get(
                account.getTransactionHistory().size() - 1
        ).contains("Converted balance to: 150.0 EUR"));
    }

    @Test
    public void testConvertBalance_DivisionMutant() {
        BankAccount account = new BankAccount("123", 100.0, "USD", 0.0, "1234");

        account.convertBalance(0.5, "EUR");

        assertEquals(50.0, account.getBalance(), 0.001);

        // Verify transaction log
        assertTrue(account.getTransactionHistory().get(
                account.getTransactionHistory().size() - 1
        ).contains("Converted balance to: 50.0 EUR"));
    }

}