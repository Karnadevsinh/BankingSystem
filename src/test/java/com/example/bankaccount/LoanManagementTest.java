package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanManagementTest {
    private LoanManagement loanManagement;

    @BeforeEach
    void setUp() {
        loanManagement = new LoanManagement();
    }

    @Test
    void testLoanApplication() {
        loanManagement.applyForLoan("A001", 1000, 0.1, 12);
        Loan loan = loanManagement.getLoanDetails("A001");
        assertNotNull(loan);
        assertEquals(1100, loan.getOutstandingAmount(), 0.01);
    }

    @Test
    void testLoanRepayment() {
        loanManagement.applyForLoan("A001", 1000, 0.1, 12);
        loanManagement.repayLoan("A001", 500);
        Loan loan = loanManagement.getLoanDetails("A001");
        assertEquals(600, loan.getOutstandingAmount(), 0.01);
    }

    @Test
    void testFullRepayment() {
        loanManagement.applyForLoan("A001", 1000, 0.1, 12);
        loanManagement.repayLoan("A001", 1100);
        assertNull(loanManagement.getLoanDetails("A001"));
    }

    @Test
    public void testRepay_ExactBoundaryAmount() {
        Loan loan = new Loan("123", 1000.0, 0.1, 12);
        double totalPayable = loan.getOutstandingAmount();

        loan.repay(totalPayable);

        assertEquals(0.0, loan.getOutstandingAmount(), 0.001);
    }

    @Test
    public void testRepay_ZeroAmount() {
        Loan loan = new Loan("123", 1000.0, 0.1, 12);
        assertThrows(IllegalArgumentException.class, () -> loan.repay(0.0));
    }

    @Test
    void getPrincipal_ReturnsSameValueAsConstructor() {
        // Arrange
        double expectedPrincipal = 10000.0;
        Loan loan = new Loan("ACC123", expectedPrincipal, 0.05, 12);

        // Act
        double actualPrincipal = loan.getPrincipal();

        // Assert
        assertEquals(expectedPrincipal, actualPrincipal,
                "Principal returned should match the value provided in constructor");
    }

//    @ParameterizedTest
//    @ValueSource(doubles = {1000.0, 5000.0, 10000.0, 50000.0})
//    void getPrincipal_ReturnsCorrectValueForDifferentAmounts(double principal) {
//        // Arrange
//        Loan loan = new Loan("ACC123", principal, 0.05, 12);
//
//        // Act
//        double result = loan.getPrincipal();
//
//        // Assert
//        assertAll(
//                () -> assertEquals(principal, result, "Principal should match the input value"),
//                () -> assertNotEquals(0.0, result, "Principal should not be 0.0"),
//                () -> assertTrue(result > 0.0, "Principal should be positive")
//        );
//    }

    @Test
    void getPrincipal_RetainsSameValueAfterRepayment() {
        // Arrange
        double principal = 10000.0;
        Loan loan = new Loan("ACC123", principal, 0.05, 12);

        // Act
        double principalBeforeRepayment = loan.getPrincipal();
        loan.repay(5000.0);
        double principalAfterRepayment = loan.getPrincipal();

        // Assert
        assertEquals(principalBeforeRepayment, principalAfterRepayment,
                "Principal should remain unchanged after repayment");
        assertNotEquals(0.0, principalAfterRepayment,
                "Principal should not be 0.0 after repayment");
    }

    @Test
    void getPrincipal_ValueUsedInTotalPayableCalculation() {
        // Arrange
        double principal = 10000.0;
        double interestRate = 0.05;
        int tenureMonths = 12;
        Loan loan = new Loan("ACC123", principal, interestRate, tenureMonths);

        // Act
        double outstandingAmount = loan.getOutstandingAmount();
        double expectedTotal = principal + (principal * interestRate * tenureMonths / 12);

        // Assert
        assertEquals(expectedTotal, outstandingAmount, 0.01,
                "Outstanding amount should be calculated using the correct principal");
        assertTrue(outstandingAmount > loan.getPrincipal(),
                "Outstanding amount should be greater than principal due to interest");
    }
}
