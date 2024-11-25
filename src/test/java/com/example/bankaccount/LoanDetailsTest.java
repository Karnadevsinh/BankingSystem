package com.example.bankaccount;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanDetailsTest {
    @Test
    public void testInterestRateMultiplicationAndDivision() {
        Loan loan = new Loan("123", 1000.0, 0.05, 12);

        assertEquals(5.0, loan.getInterestRate() * 100, 0.001);
        assertEquals(0.05, loan.getInterestRate(), 0.001);
    }
}