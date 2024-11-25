package com.example.bankaccount;

public enum AccountType {
    SAVINGS,
    CURRENT;

    public double getMinimumBalance() {
        switch (this) {
            case SAVINGS:
                return 500.0;
            case CURRENT:
                return 0.0;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public double getOverdraftLimit() {
        switch (this) {
            case CURRENT:
                return 1000.0;
            case SAVINGS:
                return 0.0;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
