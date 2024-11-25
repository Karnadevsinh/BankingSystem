package com.example.bankaccount;

import java.util.HashMap;
import java.util.Map;

public class LoanManagement {
    private final Map<String, Loan> loans;

    public LoanManagement() {
        loans = new HashMap<>();
    }

    public void applyForLoan(String accountId, double amount, double interestRate, int tenureMonths) {
        if (loans.containsKey(accountId)) {
            throw new IllegalStateException("Existing loan detected. Repay before applying for a new loan.");
        }
        loans.put(accountId, new Loan(accountId, amount, interestRate, tenureMonths));
    }

    public void repayLoan(String accountId, double payment) {
        Loan loan = loans.get(accountId);
        if (loan == null) {
            throw new IllegalArgumentException("No active loan for this account");
        }
        loan.repay(payment);
        if (loan.getOutstandingAmount() <= 0) {
            loans.remove(accountId);
        }
    }

    public Loan getLoanDetails(String accountId) {
        return loans.get(accountId);
    }
}

class Loan {
    private final String accountId;
    private final double principal;
    private final double interestRate;
    private final int tenureMonths;
    private double outstandingAmount;

    public Loan(String accountId, double principal, double interestRate, int tenureMonths) {
        this.accountId = accountId;
        this.principal = principal;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.outstandingAmount = calculateTotalPayable();
    }

    private double calculateTotalPayable() {
        return principal + (principal * interestRate * tenureMonths / 12);
    }

    public void repay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Repayment amount must be positive");
        }
        outstandingAmount -= amount;
    }

    public double getOutstandingAmount() {
        return outstandingAmount;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
