package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AccountTypeTest {
    @Test
    void testSavingsAccountRules() {
        AccountType savings = AccountType.SAVINGS;
        assertEquals(500.0, savings.getMinimumBalance());
        assertEquals(0.0, savings.getOverdraftLimit());
    }

    @Test
    void testCurrentAccountRules() {
        AccountType current = AccountType.CURRENT;
        assertEquals(0.0, current.getMinimumBalance());
        assertEquals(1000.0, current.getOverdraftLimit());
    }

    @Test
    public void testGetMinimumBalance() {
        assertEquals(500.0, AccountType.SAVINGS.getMinimumBalance(), 0.0);
        assertEquals(0.0, AccountType.CURRENT.getMinimumBalance(), 0.0);

        // Test relationship
        assertTrue(AccountType.SAVINGS.getMinimumBalance() >
                AccountType.CURRENT.getMinimumBalance());
    }

    @Test
    public void testGetOverdraftLimit() {
        assertEquals(1000.0, AccountType.CURRENT.getOverdraftLimit(), 0.0);
        assertEquals(0.0, AccountType.SAVINGS.getOverdraftLimit(), 0.0);

        // Test relationship
        assertTrue(AccountType.CURRENT.getOverdraftLimit() >
                AccountType.SAVINGS.getOverdraftLimit());
    }
}
