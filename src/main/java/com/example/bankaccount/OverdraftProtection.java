package com.example.bankaccount;

public class OverdraftProtection {

    public boolean canWithdraw(double amount, double balance, double overdraftLimit) {
        return amount > 0 && (balance + overdraftLimit) >= amount;
    }
}

