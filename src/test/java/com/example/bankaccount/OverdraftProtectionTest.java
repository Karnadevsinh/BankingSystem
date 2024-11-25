package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OverdraftProtectionTest {

    @Test
    public void testCanWithdraw_PositiveAmount_SufficientFundsWithoutOverdraft() {
        OverdraftProtection protection = new OverdraftProtection();

        // Test case to kill boundary mutants and ensure positive amount works
        assertTrue(protection.canWithdraw(50.0, 100.0, 0.0),
                "Should allow withdrawal when balance covers amount");
    }

    @Test
    public void testCanWithdraw_PositiveAmount_WithOverdraftLimit() {
        OverdraftProtection protection = new OverdraftProtection();

        // Test case to kill boundary mutants with overdraft
        assertTrue(protection.canWithdraw(150.0, 100.0, 50.0),
                "Should allow withdrawal when balance + overdraft covers amount");
    }

    @Test
    public void testCanWithdraw_ZeroAmount() {
        OverdraftProtection protection = new OverdraftProtection();

        // Test zero amount to kill boundary and conditional mutants
        assertFalse(protection.canWithdraw(0.0, 100.0, 50.0),
                "Should not allow zero amount withdrawal");
    }

    @Test
    public void testCanWithdraw_NegativeAmount() {
        OverdraftProtection protection = new OverdraftProtection();

        // Test negative amount to kill boundary and conditional mutants
        assertFalse(protection.canWithdraw(-50.0, 100.0, 50.0),
                "Should not allow negative amount withdrawal");
    }

    @Test
    public void testCanWithdraw_InsufficientFundsWithOverdraft() {
        OverdraftProtection protection = new OverdraftProtection();

        // Test case to kill mutants related to insufficient funds
        assertFalse(protection.canWithdraw(200.0, 100.0, 50.0),
                "Should not allow withdrawal exceeding balance and overdraft");
    }
}