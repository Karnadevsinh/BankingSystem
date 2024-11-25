package com.example.bankaccount;

public class InterestCalculator {
    private static final double SAVINGS_INTEREST_RATE = 0.03; // 3% annual
    private static final int DAYS_IN_YEAR = 365;

    public double calculateDailyInterest(double balance) {
        return balance * (SAVINGS_INTEREST_RATE / DAYS_IN_YEAR);
    }

    public double applyInterest(double balance, int days) {
        return balance + (calculateDailyInterest(balance) * days);
    }
}
