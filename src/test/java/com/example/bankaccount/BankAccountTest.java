package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BankAccountTest {
    private BankAccount account;
    private static final String ACCOUNT_ID = "ACC123";
    private static final String CURRENCY = "USD";
    private static final double INITIAL_BALANCE = 1000.0;
    private static final double OVERDRAFT_LIMIT = 500.0;
    private static final String PIN = "1234";

    @BeforeEach
    void setUp() {
        account = new BankAccount(ACCOUNT_ID, INITIAL_BALANCE, CURRENCY, OVERDRAFT_LIMIT, PIN);
    }
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

    @Test
    void getAccountId_ReturnsCorrectValue() {
        // Act
        String result = account.getAccountId();

        // Assert
        assertAll(
                () -> assertEquals(ACCOUNT_ID, result, "Account ID should match constructor value"),
                () -> assertNotEquals("", result, "Account ID should not be empty"),
                () -> assertNotNull(result, "Account ID should not be null")
        );
    }

    @Test
    void getCurrency_ReturnsCorrectValue() {
        // Act
        String result = account.getCurrency();

        // Assert
        assertAll(
                () -> assertEquals(CURRENCY, result, "Currency should match constructor value"),
                () -> assertNotEquals("", result, "Currency should not be empty"),
                () -> assertNotNull(result, "Currency should not be null")
        );
    }

    @Test
    void isLocked_InitiallyFalse() {
        // Act & Assert
        assertFalse(account.isLocked(), "Account should not be locked initially");
    }

    @Test
    void isLocked_TrueAfterLocking() {
        // Act
        account.lockAccount();

        // Assert
        assertTrue(account.isLocked(), "Account should be locked after lockAccount()");
    }

    @Test
    void isLocked_FalseAfterUnlocking() {
        // Arrange
        account.lockAccount();

        // Act
        account.unlockAccount(PIN);

        // Assert
        assertFalse(account.isLocked(), "Account should be unlocked after unlockAccount()");
    }

    @Test
    void transfer_SuccessfulTransfer() {
        // Arrange
        BankAccount targetAccount = new BankAccount("ACC456", 0.0, CURRENCY, 0.0, PIN);
        double transferAmount = 500.0;

        // Act
        boolean result = account.transfer(targetAccount, transferAmount, PIN);

        // Assert
        assertAll(
                () -> assertTrue(result, "Transfer should return true on success"),
                () -> assertEquals(INITIAL_BALANCE - transferAmount, account.getBalance(), "Source account balance should be reduced"),
                () -> assertEquals(transferAmount, targetAccount.getBalance(), "Target account should receive the money")
        );
    }

    @Test
    void transfer_FailsWithInsufficientFunds() {
        // Arrange
        BankAccount targetAccount = new BankAccount("ACC456", 0.0, CURRENCY, 0.0, PIN);
        double transferAmount = INITIAL_BALANCE + 1.0;

        // Act
        boolean result = account.transfer(targetAccount, transferAmount, PIN);

        // Assert
        assertAll(
                () -> assertFalse(result, "Transfer should return false with insufficient funds"),
                () -> assertEquals(INITIAL_BALANCE, account.getBalance(), "Source account balance should remain unchanged"),
                () -> assertEquals(0.0, targetAccount.getBalance(), "Target account should not receive any money")
        );
    }

    @Test
    void transfer_FailsWithIncorrectPin() {
        // Arrange
        BankAccount targetAccount = new BankAccount("ACC456", 0.0, CURRENCY, 0.0, PIN);

        // Act
        boolean result = account.transfer(targetAccount, 500.0, "wrong_pin");

        // Assert
        assertAll(
                () -> assertFalse(result, "Transfer should return false with incorrect PIN"),
                () -> assertEquals(INITIAL_BALANCE, account.getBalance(), "Source account balance should remain unchanged"),
                () -> assertEquals(0.0, targetAccount.getBalance(), "Target account should not receive any money")
        );
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String result = account.toString();

        // Assert
        assertAll(
                () -> assertNotEquals("", result, "toString should not return empty string"),
                () -> assertTrue(result.contains(ACCOUNT_ID), "toString should contain account ID"),
                () -> assertTrue(result.contains(CURRENCY), "toString should contain currency"),
                () -> assertTrue(result.contains(String.valueOf(INITIAL_BALANCE)), "toString should contain balance"),
                () -> assertTrue(result.contains(String.valueOf(OVERDRAFT_LIMIT)), "toString should contain overdraft limit")
        );
    }

    @Test
    void getOverdraftLimit_ReturnsCorrectValue() {
        // Act
        double result = account.getOverdraftLimit();

        // Assert
        assertAll(
                () -> assertEquals(OVERDRAFT_LIMIT, result, "Overdraft limit should match constructor value"),
                () -> assertNotEquals(0.0, result, "Overdraft limit should not be 0.0"),
                () -> assertTrue(result > 0, "Overdraft limit should be positive")
        );
    }

    @Test
    void transfer_UpdatesTransactionHistory() {
        // Arrange
        BankAccount targetAccount = new BankAccount("ACC456", 0.0, CURRENCY, 0.0, PIN);
        double transferAmount = 500.0;

        // Act
        account.transfer(targetAccount, transferAmount, PIN);
        List<String> history = account.getTransactionHistory();

        // Assert
        assertTrue(history.stream()
                        .anyMatch(transaction -> transaction.contains("Transferred: " + transferAmount)
                                && transaction.contains(targetAccount.getAccountId())),
                "Transaction history should contain transfer details");
    }

}