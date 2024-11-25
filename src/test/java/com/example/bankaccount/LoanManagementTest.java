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
}
