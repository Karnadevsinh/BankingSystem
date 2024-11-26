package com.example.bankaccount;

public enum AccountType {
    SAVINGS {
        @Override
        public double getMinimumBalance() {
            return 500.0;
        }

        @Override
        public double getOverdraftLimit() {
            return 0.0;
        }
    },
    CURRENT {
        @Override
        public double getMinimumBalance() {
            return 0.0;
        }

        @Override
        public double getOverdraftLimit() {
            return 1000.0;
        }
    };

    public abstract double getMinimumBalance();
    public abstract double getOverdraftLimit();
}