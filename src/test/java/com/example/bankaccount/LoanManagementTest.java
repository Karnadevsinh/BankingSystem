package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class LoanManagementTest {
    private LoanManagement loanManagement;
    private static final String ACCOUNT_ID = "ACC123";
    private static final double AMOUNT = 10000.0;
    private static final double INTEREST_RATE = 0.1; // 10%
    private static final int TENURE_MONTHS = 12;
    private static final double PRINCIPAL = 10000.0;


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

    // Test for survived mutant: removed call to Map::containsKey
    @Test
    void testApplyForLoan_ExistingLoan_ThrowsException() {
        // First loan application
        loanManagement.applyForLoan(ACCOUNT_ID, AMOUNT, INTEREST_RATE, TENURE_MONTHS);

        // Second loan application should throw exception
        assertThrows(IllegalStateException.class, () ->
                loanManagement.applyForLoan(ACCOUNT_ID, AMOUNT, INTEREST_RATE, TENURE_MONTHS)
        );

        // Verify the exception message
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                loanManagement.applyForLoan(ACCOUNT_ID, AMOUNT, INTEREST_RATE, TENURE_MONTHS)
        );
        assertEquals("Existing loan detected. Repay before applying for a new loan.",
                exception.getMessage());
    }

    // Test for survived mutant: replaced equality check with false in repayLoan
    @Test
    void testRepayLoan_NoActiveLoan_ThrowsException() {
        // Attempt to repay non-existent loan
        assertThrows(IllegalArgumentException.class, () ->
                loanManagement.repayLoan("NON_EXISTENT", 1000.0)
        );

        // Verify the exception message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                loanManagement.repayLoan("NON_EXISTENT", 1000.0)
        );
        assertEquals("No active loan for this account", exception.getMessage());
    }

    // Test for survived mutant: Substituted 0.0 with 1.0 in loan.getOutstandingAmount() <= 0
    @Test
    void testRepayLoan_FullRepayment_RemovesLoan() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, AMOUNT, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);
        double totalPayable = loan.getOutstandingAmount();

        // Repay the full amount
        loanManagement.repayLoan(ACCOUNT_ID, totalPayable);

        // Verify loan is removed
        assertNull(loanManagement.getLoanDetails(ACCOUNT_ID));

        // Verify attempting to repay again throws exception
        assertThrows(IllegalArgumentException.class, () ->
                loanManagement.repayLoan(ACCOUNT_ID, 100.0)
        );
    }

    // Test for survived mutant: Substituted 0.0 with 1.0 in amount <= 0 check
    @Test
    void testLoanRepay_NonPositiveAmount_ThrowsException() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, AMOUNT, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);

        // Test zero amount
        assertThrows(IllegalArgumentException.class, () ->
                loan.repay(0.0)
        );

        // Test negative amount
        assertThrows(IllegalArgumentException.class, () ->
                loan.repay(-100.0)
        );

        // Verify the exception message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                loan.repay(0.0)
        );
        assertEquals("Repayment amount must be positive", exception.getMessage());
    }

    // Additional test to verify partial repayment behavior
    @Test
    void testRepayLoan_PartialRepayment_UpdatesOutstandingAmount() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, AMOUNT, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);
        double initialOutstanding = loan.getOutstandingAmount();
        double partialPayment = 1000.0;

        // Make partial repayment
        loanManagement.repayLoan(ACCOUNT_ID, partialPayment);

        // Verify outstanding amount is updated correctly
        assertEquals(initialOutstanding - partialPayment,
                loan.getOutstandingAmount(), 0.001);

        // Verify loan still exists
        assertNotNull(loanManagement.getLoanDetails(ACCOUNT_ID));
    }

    @Test
    void repayLoan_exactlyZeroOutstanding_shouldRemoveLoan() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, PRINCIPAL, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);
        double totalAmount = loan.getOutstandingAmount();

        // Make payment that makes outstanding exactly 0
        loanManagement.repayLoan(ACCOUNT_ID, totalAmount);

        // Verify loan is removed
        assertNull(loanManagement.getLoanDetails(ACCOUNT_ID),
                "Loan should be removed when outstanding amount is exactly 0");
    }

    @Test
    void repayLoan_slightlyNegativeOutstanding_shouldRemoveLoan() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, PRINCIPAL, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);
        double totalAmount = loan.getOutstandingAmount();

        // Make payment slightly larger than outstanding amount
        loanManagement.repayLoan(ACCOUNT_ID, totalAmount + 0.01);

        // Verify loan is removed
        assertNull(loanManagement.getLoanDetails(ACCOUNT_ID),
                "Loan should be removed when outstanding amount is negative");
    }

    @Test
    void repay_exactlyZeroAmount_shouldThrowException() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, PRINCIPAL, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);

        // Attempt to repay exactly 0
        assertThrows(IllegalArgumentException.class,
                () -> loan.repay(0.0),
                "Should throw exception when repayment amount is exactly 0");
    }

    @Test
    void repay_slightlyPositiveAmount_shouldSucceed() {
        // Setup
        loanManagement.applyForLoan(ACCOUNT_ID, PRINCIPAL, INTEREST_RATE, TENURE_MONTHS);
        Loan loan = loanManagement.getLoanDetails(ACCOUNT_ID);
        double initialOutstanding = loan.getOutstandingAmount();
        double smallAmount = 0.01;

        // Repay very small amount
        loan.repay(smallAmount);

        // Verify the payment was processed
        assertEquals(initialOutstanding - smallAmount, loan.getOutstandingAmount(),
                "Small positive payment should reduce outstanding amount");
    }

    @Test
    public void testLoanShouldNotBeRemovedWhenOutstandingAmountIsSlightlyPositive() {
        // Arrange
        LoanManagement loanManagement = new LoanManagement();
        String accountId = "TEST001";
        double principal = 1000.0;
        double interestRate = 0.10; // 10%
        int tenureMonths = 12;

        // Apply for loan
        loanManagement.applyForLoan(accountId, principal, interestRate, 12);

        // Get total payable amount and pay all except 0.5
        Loan loan = loanManagement.getLoanDetails(accountId);
        double totalPayable = loan.getOutstandingAmount();
        double partialPayment = totalPayable - 0.5;  // Leave 0.5 outstanding

        // Act
        loanManagement.repayLoan(accountId, partialPayment);

        // Assert
        assertNotNull(loanManagement.getLoanDetails(accountId), "Loan should still exist when outstanding amount is positive");

        assertEquals(0.5, loanManagement.getLoanDetails(accountId).getOutstandingAmount(), 0.0001);
    }
}