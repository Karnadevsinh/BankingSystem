package com.example.bankaccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BankingIntegrationTest {
    private BankAccount savingsAccount;
    private BankAccount checkingAccount;
    private CurrencyConverter currencyConverter;
    private LoanManagement loanManagement;
    private ScheduledTransfer scheduledTransfer;
    private InterestCalculator interestCalculator;
    private OverdraftProtection overdraftProtection;

    @BeforeEach
    void setUp() {
        // Initialize components
        currencyConverter = new CurrencyConverter();
        loanManagement = new LoanManagement();
        scheduledTransfer = new ScheduledTransfer();
        interestCalculator = new InterestCalculator();
        overdraftProtection = new OverdraftProtection();

        // Create accounts
        savingsAccount = new BankAccount("SAV001", 5000.0, "USD", 0.0, "1234");
        checkingAccount = new BankAccount("CHK001", 3000.0, "USD", 1000.0, "5678");
    }

    @Test
    void currencyConversionScenario() {
        // Test currency conversion
        double eurAmount = currencyConverter.convert("USD", "EUR", 1000.0);

        assertAll(
                () -> assertTrue(eurAmount > 0, "Converted amount should be positive"),
                () -> assertNotEquals(1000.0, eurAmount, "Conversion should change the amount")
        );
    }

    @Test
    void loanManagementScenario() {
        // Apply for a loan
        loanManagement.applyForLoan("SAV001", 10000.0, 0.05, 12);

        // Get loan details
        Loan loan = loanManagement.getLoanDetails("SAV001");

        assertAll(
                () -> assertNotNull(loan, "Loan should be created"),
                () -> assertEquals(10000.0, loan.getPrincipal(), "Loan principal should match"),
                () -> assertEquals(0.05, loan.getInterestRate(), "Loan interest rate should match")
        );

        // Partial loan repayment
        double initialOutstanding = loan.getOutstandingAmount();
        loanManagement.repayLoan("SAV001", 2000.0);

        assertTrue(loan.getOutstandingAmount() < initialOutstanding,
                "Outstanding amount should decrease after repayment");
    }

    @Test
    void scheduledTransferScenario() {
        // Schedule a transfer
        LocalDate transferDate = LocalDate.now().plusDays(1);
        scheduledTransfer.scheduleTransfer("SAV001", "CHK001", 1000.0, transferDate);

        // Check scheduled transfers
        List<ScheduledTransfer.Transfer> transfers = scheduledTransfer.getScheduledTransfers();

        assertAll(
                () -> assertFalse(transfers.isEmpty(), "Transfer should be scheduled"),
                () -> assertEquals(1, transfers.size(), "Only one transfer should be scheduled"),
                () -> assertEquals("SAV001", transfers.get(0).fromAccount, "From account should match"),
                () -> assertEquals("CHK001", transfers.get(0).toAccount, "To account should match"),
                () -> assertEquals(1000.0, transfers.get(0).amount, "Transfer amount should match")
        );
    }

    @Test
    void overdraftProtectionScenario() {
        // Test overdraft protection
        boolean canWithdraw = overdraftProtection.canWithdraw(
                3500.0,
                checkingAccount.getBalance(),
                checkingAccount.getOverdraftLimit()
        );

        assertTrue(canWithdraw, "Should allow withdrawal within overdraft limit");
    }

    @Test
    void interestCalculationScenario() {
        // Calculate interest
        double dailyInterest = interestCalculator.calculateDailyInterest(savingsAccount.getBalance());
        double balanceAfterInterest = interestCalculator.applyInterest(savingsAccount.getBalance(), 30);

        assertAll(
                () -> assertTrue(dailyInterest > 0, "Daily interest should be positive"),
                () -> assertTrue(balanceAfterInterest > savingsAccount.getBalance(),
                        "Balance should increase with interest")
        );
    }

    @Test
    void complexBankingWorkflowScenario() {
        // Apply for a loan
        loanManagement.applyForLoan("SAV001", 10000.0, 0.05, 12);

        // Schedule a transfer
        LocalDate transferDate = LocalDate.now().plusDays(1);
        scheduledTransfer.scheduleTransfer("SAV001", "CHK001", 2000.0, transferDate);

        // Convert currency
        double eurAmount = currencyConverter.convert("USD", "EUR", 1000.0);

        assertAll(
                () -> assertNotNull(loanManagement.getLoanDetails("SAV001"), "Loan should exist"),
                () -> assertFalse(scheduledTransfer.getScheduledTransfers().isEmpty(), "Transfer should be scheduled"),
                () -> assertTrue(eurAmount > 0, "Currency conversion should work")
        );
    }
}