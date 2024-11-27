package com.example.bankaccount;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private final String accountId;
    private double balance;
    private final String currency;
    private double overdraftLimit;
    private final List<String> transactionHistory;
    private String pin;
    private boolean isLocked;


    public BankAccount(String accountId, double balance, String currency, double overdraftLimit, String pin) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void lockAccount() {
        isLocked = true;
    }

    public void unlockAccount(String pin) {
        if (authenticate(pin)) {
            isLocked = false;
        } else {
            throw new SecurityException("Incorrect PIN. Unable to unlock the account.");
        }
    }

    public boolean deposit(double amount, String pin) {
        if (!authenticate(pin)) return false;
        if (amount > 0) {
            balance += amount;
            logTransaction("Deposited: " + amount);
            return true;
        }
        return false;
    }

    // Clear transaction history for testing
    public void clearTransactionHistory() {
        this.transactionHistory.clear();
    }

    // Log transaction method
    public void logTransaction(String transactionDetails) {
        transactionHistory.add(transactionDetails);
    }

    public boolean withdraw(double amount, String pin) {
        if (!authenticate(pin)) return false;
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            logTransaction("Withdrew: " + amount);
            return true;
        }
        return false;
    }


    public boolean transfer(BankAccount targetAccount, double amount, String pin) {
        if (this.withdraw(amount, pin)) {  // Withdraw from the source account
            targetAccount.deposit(amount, pin);  // Deposit into the target account
            logTransaction("Transferred: " + amount + " to " + targetAccount.getAccountId());
            return true;
        }
        return false;
    }


    public void convertBalance(double rate, String targetCurrency) {
        balance *= rate;
        logTransaction("Converted balance to: " + balance + " " + targetCurrency);
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public boolean authenticate(String pin) {
        return this.pin.equals(pin);
    }

    public void changePin(String oldPin, String newPin) {
        if (authenticate(oldPin)) {
            this.pin = newPin;
        } else {
            throw new SecurityException("Incorrect old PIN.");
        }
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountId='" + accountId + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", overdraftLimit=" + overdraftLimit +
                '}';
    }

    // Getter and Setter for overdraftLimit
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

//    public void setOverdraftLimit(double overdraftLimit) {
//        this.overdraftLimit = overdraftLimit;
//    }
}
