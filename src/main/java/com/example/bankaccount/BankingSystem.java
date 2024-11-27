package com.example.bankaccount;

import java.time.LocalDate;
import java.util.Scanner;

public class BankingSystem {
    private final CurrencyConverter currencyConverter;
    private final LoanManagement loanManagement;
    private final ScheduledTransfer scheduledTransfer;

    public BankingSystem() {
        currencyConverter = new CurrencyConverter();
        loanManagement = new LoanManagement();
        scheduledTransfer = new ScheduledTransfer();
    }

    public void displayMenu() {
        System.out.println("Welcome to the Banking System");
        System.out.println("1. Convert Currency");
        System.out.println("2. Apply for Loan");
        System.out.println("3. Repay Loan");
        System.out.println("4. View Loan Details");
        System.out.println("5. Schedule a Transfer");
        System.out.println("6. Execute Scheduled Transfers");
        System.out.println("7. Exit");
    }

    public void convertCurrency() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter source currency (e.g., USD): ");
        String fromCurrency = scanner.nextLine();

        System.out.print("Enter target currency (e.g., INR): ");
        String toCurrency = scanner.nextLine();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            double convertedAmount = currencyConverter.convert(fromCurrency, toCurrency, amount);
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, toCurrency);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void applyForLoan() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();

        System.out.print("Enter Loan Amount: ");
        double amount = scanner.nextDouble();

        System.out.print("Enter Interest Rate (as a decimal, e.g., 0.1 for 10%): ");
        double interestRate = scanner.nextDouble();

        System.out.print("Enter Tenure (in months): ");
        int tenure = scanner.nextInt();

        try {
            loanManagement.applyForLoan(accountId, amount, interestRate, tenure);
            System.out.println("Loan application successful.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void repayLoan() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();

        System.out.print("Enter Repayment Amount: ");
        double payment = scanner.nextDouble();

        try {
            loanManagement.repayLoan(accountId, payment);
            System.out.println("Loan repayment successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewLoanDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();

        Loan loan = loanManagement.getLoanDetails(accountId);
        if (loan != null) {
            System.out.println("Loan Details:");
            System.out.printf("Principal: %.2f%n", loan.getPrincipal());
            System.out.printf("Interest Rate: %.2f%%%n", loan.getInterestRate() * 100);
            System.out.printf("Outstanding Amount: %.2f%n", loan.getOutstandingAmount());
        } else {
            System.out.println("No active loan for this account.");
        }
    }

    public void scheduleTransfer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Source Account ID: ");
        String fromAccount = scanner.nextLine();

        System.out.print("Enter Target Account ID: ");
        String toAccount = scanner.nextLine();

        System.out.print("Enter Amount to Transfer: ");
        double amount = scanner.nextDouble();

        System.out.print("Enter Transfer Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.next());

        try {
            scheduledTransfer.scheduleTransfer(fromAccount, toAccount, amount, date);
            System.out.println("Transfer scheduled successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void executeScheduledTransfers() {
        System.out.println("Executing scheduled transfers for today...");
        scheduledTransfer.executeDueTransfers();
        System.out.println("Done.");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> convertCurrency();
                case 2 -> applyForLoan();
                case 3 -> repayLoan();
                case 4 -> viewLoanDetails();
                case 5 -> scheduleTransfer();
                case 6 -> executeScheduledTransfers();
                case 7 -> {
                    System.out.println("Exiting the Banking System. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        bankingSystem.start();
    }
}
