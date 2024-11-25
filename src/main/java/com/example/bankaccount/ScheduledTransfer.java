package com.example.bankaccount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduledTransfer {
    private final List<Transfer> transfers;

    public ScheduledTransfer() {
        this.transfers = new ArrayList<>();
    }

    public void scheduleTransfer(String fromAccount, String toAccount, double amount, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot schedule transfers in the past");
        }
        transfers.add(new Transfer(fromAccount, toAccount, amount, date));
    }

    public List<Transfer> getScheduledTransfers() {
        return new ArrayList<>(transfers);
    }

    public void executeDueTransfers() {
        LocalDate today = LocalDate.now();
        transfers.removeIf(transfer -> {
            if (transfer.date.isEqual(today)) {
                System.out.println("Executed transfer: " + transfer);
                return true;
            }
            return false;
        });
    }

    static class Transfer {
        String fromAccount;
        String toAccount;
        double amount;
        LocalDate date;

        public Transfer(String fromAccount, String toAccount, double amount, LocalDate date) {
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
            this.date = date;
        }

        @Override
        public String toString() {
            return "Transfer from " + fromAccount + " to " + toAccount + " of " + amount + " on " + date;
        }
    }
}
