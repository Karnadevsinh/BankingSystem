
# 🏦 **Banking System Project**  

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)  
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white)  
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![Mutation Testing](https://img.shields.io/badge/Mutation_Testing-Strongly_Killed-success?style=for-the-badge)

---

## 📖 **Project Overview**  

The **Banking System Project** is a robust, modular, and extensible platform designed for managing core banking operations. With secure authentication, flexible account management, and advanced features like loan management and multi-currency support, this system is tailored for real-world banking needs.  

---

## 🚀 **Features**

| **Feature**                      | **Description**                                                                                  |
|-----------------------------------|--------------------------------------------------------------------------------------------------|
| 💳 **Account Management**         | Support for Savings, Current, and Fixed Deposit accounts with unique rules.                     |
| 🔒 **Secure Authentication**      | PIN-based system with account locking after multiple failed attempts.                           |
| 💵 **Transaction Handling**       | Perform deposits, withdrawals, transfers, and maintain detailed transaction histories.           |
| 🌎 **Multi-Currency Support**     | Handle accounts in multiple currencies with real-time exchange rates.                           |
| 📅 **Scheduled Transfers**        | Schedule future transfers and automate their execution.                                         |
| 🏦 **Loan Management**            | Apply for loans, repay them, and track overdue payments.                                         |
| 📊 **Account Statements**         | Generate monthly statements summarizing balances, transactions, and loan statuses.              |
| 📈 **Interest Calculation**       | Automatically apply interest based on account type and balance.                                 |

---

## 📂 **Project Structure**

```plaintext
BankingSystem/
├── src/
│   ├── main/
│   │   ├── java/com/example/bankaccount/
│   │   │   ├── AccountType.java
│   │   │   ├── BankAccount.java
│   │   │   ├── BankingSystem.java
│   │   │   ├── CurrencyConverter.java
│   │   │   ├── InterestCalculator.java
│   │   │   ├── LoanManagement.java
│   │   │   ├── OverdraftProtection.java
│   │   │   ├── ScheduledTransfer.java
│   │   │   ├── TransactionHistory.java
│   │   └── resources/
│   ├── test/
│   │   ├── java/com/example/bankaccount/
│   │   │   ├── AccountTypeTest.java
│   │   │   ├── BankAccountTest.java
│   │   │   ├── BankingSystemTest.java
│   │   │   ├── CurrencyConverterTest.java
│   │   │   ├── InterestCalculatorTest.java
│   │   │   ├── LoanManagementTest.java
│   │   │   ├── OverdraftProtectionTest.java
│   │   │   ├── ScheduledTransferTest.java
│   │   │   ├── TransactionHistoryTest.java
├── README.md
```

---

## ⚙️ **Installation & Setup**

1. Clone this repository:
   ```bash
   git clone https://github.com/username/BankingSystem.git
   cd BankingSystem
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run Unit tests:
   ```bash
   mvn test
   ```
4. Run Integration tests:
   ```bash
   mvn verify
   ```
---

## 🛠️ **Tech Stack**

- **Language**: Java  
- **Framework**: JUnit 5 for Testing 
- **Testing**: Mutation Testing
- **Build Tool**: Maven  
- **Version Control**: Git

---
## 🛠️ **Testing Analysis**
![img.png](/Users/karnadevsinhzala/Desktop/Screenshot 2024-11-26 at 9.10.15 PM.png)
![img_1.png](/Users/karnadevsinhzala/Desktop/Screenshot 2024-11-26 at 9.10.52 PM.png)
![img_2.png](/Users/karnadevsinhzala/Desktop/Screenshot 2024-11-26 at 9.11.15 PM.png)
---

## 🌟 **Usage**

- Create and manage multiple accounts securely.
- Perform transactions, schedule transfers, and view transaction histories.
- Handle loans and apply interest to savings accounts.
- Utilize multi-currency accounts with real-time exchange rates.

---

## 👩‍💻 **Authors**

- [Karnadevsinh Zala](https://github.com/Karnadevsinh)  
- [Somesh Awasthi](https://github.com/somesh-awasthi)

