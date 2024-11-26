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

    private static final String INITIAL_PIN = "1234";

    private final String CORRECT_PIN = "1234";
    private final String WRONG_PIN = "5678";

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


    @Test
    void testDepositLogsTransaction() {
        // Prepare
        double depositAmount = 500.0;

        // Act
        boolean result = account.deposit(depositAmount, INITIAL_PIN);

        // Assert
        assertTrue(result, "Deposit should be successful");

        // Verify transaction history
        List<String> history = account.getTransactionHistory();
        assertFalse(history.isEmpty(), "Transaction history should not be empty");
        assertTrue(history.get(history.size() - 1).contains("Deposited: " + depositAmount),
                "Transaction log should contain deposit details");
    }

    @Test
    void testDepositFailsWithInvalidPin() {
        // Prepare
        double depositAmount = 500.0;

        // Act
        boolean result = account.deposit(depositAmount, "wrongpin");

        // Assert
        assertFalse(result, "Deposit should fail with invalid PIN");

        // Verify transaction history
        List<String> history = account.getTransactionHistory();
        assertTrue(history.isEmpty(), "No transaction should be logged");
    }

    @Test
    void testWithdrawLogsTransaction() {
        // Prepare
        double withdrawAmount = 300.0;

        // Act
        boolean result = account.withdraw(withdrawAmount, INITIAL_PIN);

        // Assert
        assertTrue(result, "Withdrawal should be successful");

        // Verify transaction history
        List<String> history = account.getTransactionHistory();
        assertFalse(history.isEmpty(), "Transaction history should not be empty");
        assertTrue(history.get(history.size() - 1).contains("Withdrew: " + withdrawAmount),
                "Transaction log should contain withdrawal details");
    }

    @Test
    void testWithdrawFailsWithInvalidPin() {
        // Prepare
        double withdrawAmount = 300.0;

        // Act
        boolean result = account.withdraw(withdrawAmount, "wrongpin");

        // Assert
        assertFalse(result, "Withdrawal should fail with invalid PIN");

        // Verify transaction history
        List<String> history = account.getTransactionHistory();
        assertTrue(history.isEmpty(), "No transaction should be logged");
    }

    @Test
    void testClearTransactionHistory() {
        // Prepare - add some transactions
        account.deposit(100.0, INITIAL_PIN);
        account.withdraw(50.0, INITIAL_PIN);

        // Verify transactions were logged
        List<String> historyBefore = account.getTransactionHistory();
        assertFalse(historyBefore.isEmpty(), "Transaction history should not be empty before clearing");

        // Act
        account.clearTransactionHistory();

        // Assert
        List<String> historyAfter = account.getTransactionHistory();
        assertTrue(historyAfter.isEmpty(), "Transaction history should be empty after clearing");
    }

    @Test
    void testChangePinSuccessful() {
        // Prepare
        String newPin = "5678";

        // Act
        account.changePin(INITIAL_PIN, newPin);

        // Assert
        assertTrue(account.authenticate(newPin), "New PIN should be set");
        assertFalse(account.authenticate(INITIAL_PIN), "Old PIN should no longer work");
    }

    @Test
    void testChangePinFailsWithWrongOldPin() {
        // Prepare
        String newPin = "5678";

        // Act & Assert
        assertThrows(SecurityException.class,
                () -> account.changePin("wrongpin", newPin),
                "Should throw SecurityException for incorrect old PIN");

        // Verify PIN remains unchanged
        assertTrue(account.authenticate(INITIAL_PIN), "Original PIN should remain");
    }


    @Test
    public void testUnlockAccount_CorrectPin() {
        // First lock the account
        account.lockAccount();
        assertTrue(account.isLocked());

        // Try to unlock with correct PIN
        account.unlockAccount(CORRECT_PIN);
        assertFalse(account.isLocked());
    }

    @Test
    public void testUnlockAccount_WrongPin() {
        // First lock the account
        account.lockAccount();
        assertTrue(account.isLocked());

        // Try to unlock with wrong PIN
        try {
            account.unlockAccount(WRONG_PIN);
            fail("Should have thrown SecurityException");
        } catch (SecurityException e) {
            assertEquals("Incorrect PIN. Unable to unlock the account.", e.getMessage());
            assertTrue(account.isLocked(), "Account should remain locked");
        }
    }

    @Test
    public void testUnlockAccount_NullPin() {
        account.lockAccount();
        assertTrue(account.isLocked());

        try {
            account.unlockAccount(null);
            fail("Should have thrown SecurityException");
        } catch (SecurityException e) {
            assertEquals("Incorrect PIN. Unable to unlock the account.", e.getMessage());
            assertTrue(account.isLocked(), "Account should remain locked");
        }
    }

    @Test
    public void testUnlockAccount_EmptyPin() {
        account.lockAccount();
        assertTrue(account.isLocked());

        try {
            account.unlockAccount("");
            fail("Should have thrown SecurityException");
        } catch (SecurityException e) {
            assertEquals("Incorrect PIN. Unable to unlock the account.", e.getMessage());
            assertTrue(account.isLocked(), "Account should remain locked");
        }
    }

    @Test
    public void testUnlockAccount_StateVerification() {
        // Test initial state
        assertFalse(account.isLocked(), "Account should be initially unlocked");

        // Lock and verify
        account.lockAccount();
        assertTrue(account.isLocked(), "Account should be locked after lockAccount()");

        // Unlock with wrong PIN and verify still locked
        try {
            account.unlockAccount(WRONG_PIN);
            fail("Should have thrown SecurityException");
        } catch (SecurityException e) {
            assertTrue(account.isLocked(), "Account should still be locked after failed unlock attempt");
        }

        // Unlock with correct PIN and verify unlocked
        account.unlockAccount(CORRECT_PIN);
        assertFalse(account.isLocked(), "Account should be unlocked after successful unlock");
    }

    @Test
    public void testUnlockAccount_MultipleAttempts() {
        account.lockAccount();

        // Try wrong PIN multiple times
        for (int i = 0; i < 3; i++) {
            try {
                account.unlockAccount(WRONG_PIN);
                fail("Should have thrown SecurityException");
            } catch (SecurityException e) {
                assertTrue(account.isLocked(),
                        "Account should remain locked after failed attempt " + (i + 1));
            }
        }

        // Verify correct PIN still works after multiple failed attempts
        account.unlockAccount(CORRECT_PIN);
        assertFalse(account.isLocked(),
                "Account should unlock with correct PIN after failed attempts");
    }

}