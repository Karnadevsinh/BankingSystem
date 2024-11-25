package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InterestCalculatorTest {
    private InterestCalculator calculator = new InterestCalculator();

    @Test
    void testCalculateDailyInterest() {
        double dailyInterest = calculator.calculateDailyInterest(1000);
        assertEquals(0.082, dailyInterest, 0.001); // 3% annual interest
    }

    @Test
    void testApplyInterest() {
        double newBalance = calculator.applyInterest(1000, 30); // 30 days
        assertEquals(1002.47, newBalance, 0.01); // Approx value
    }
}
