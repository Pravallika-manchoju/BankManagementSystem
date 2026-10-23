package controller;

import java.util.*;
import model.*;
import service.*;
import view.*;

public class BankController {
    private final BankService service;
    private final consoleView view;
    public BankController(BankService service, consoleView view) {
        this.service = service;
        this.view = view;
    }
    public void start() {
        boolean running = true;
        while (running) {
            int choice = view.showRoleMenu();
            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    adminMenu();
                    break;
                case 0:
                    running = false;
                    view.showMessage("Thank you for using our banking system.");
                    break;
                default:
                    view.showMessage("Invalid role selection.");
            }
        }
    }

    private void customerMenu() {
        boolean back = false;
        while (!back) {
            int choice = view.showCustomerMenu();
            switch (choice) {
                case 1:
                    createCustomerAccount();
                    break;
                case 2:
                    customerLogin();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    view.showMessage("Invalid option.");
            }
        }
    }

    private void createCustomerAccount() {
        String name = view.readText("Enter your name: ");
        String password = view.readText("Create a password: ");
        double initialBalance = view.readDouble("Enter initial deposit: ");
        Account account = service.createAccount(name, password, initialBalance);
        if (account != null) {
            view.showMessage("Account created successfully!");
            view.showMessage("Your account number is: " + account.getAccountNumber());
        } else {
            view.showMessage("Account creation failed. Check your details and amount.");
        }
    }

    private void customerLogin() {
        int accountNumber = view.readInt("Enter account number: ");
        String password = view.readText("Enter password: ");

        Account account = service.login(accountNumber, password);

        if (account == null) {
            view.showMessage("Invalid account number or password.");
            return;
        }

        view.showMessage("Welcome, " + account.getName() + "!");
        customerOperations(account.getAccountNumber());
    }

    private void customerOperations(int accountNumber) {
        boolean loggedIn = true;
        while (loggedIn) {
            int choice = view.showCustomerOperationsMenu();
            switch (choice) {
                case 1:
                    showBalance(accountNumber);
                    break;
                case 2:
                    depositMoney(accountNumber);
                    break;
                case 3:
                    transferMoney(accountNumber);
                    break;
                case 4:
                    showTransactionHistory(accountNumber);
                    break;
                case 5:
                    viewAccountDetails(accountNumber);
                    break;
                case 6:
                    updateAccountDetails(accountNumber);
                    break;
                case 0:
                    loggedIn = false;
                    view.showMessage("You have logged out.");
                    break;
                default:
                    view.showMessage("Invalid option.");
            }
        }
    }

    private void showBalance(int accountNumber) {
        double balance = service.getBalance(accountNumber);
        if (balance < 0) {
            view.showMessage("Account not found.");
        } else {
            view.showMessage("Current balance: " + balance);
        }
    }

    private void depositMoney(int accountNumber) {
        double amount = view.readDouble("Enter deposit amount: ");
        if (service.deposit(accountNumber, amount)) {
            view.showMessage("Deposit successful.");
            showBalance(accountNumber);
        } else {
            view.showMessage("Deposit failed. Enter a valid positive amount.");
        }
    }

    private void transferMoney(int senderAccountNumber) {
        int receiverAccountNumber = view.readInt("Enter receiver account number: ");
        double amount = view.readDouble("Enter amount to send: ");
        if (service.transfer(senderAccountNumber, receiverAccountNumber, amount)) {
            view.showMessage("Transfer successful.");
        } else {
            view.showMessage("Transfer failed. Check the receiver and available balance.");
        }
    }

    private void showTransactionHistory(int accountNumber) {
        List<Transaction> history = service.getTransactionHistory(accountNumber);
        view.showTransactionHistory(history);
    }

    private void viewAccountDetails(int accountNumber) {
        Account account = service.searchAccount(accountNumber);
        view.showAccount(account);
    }

    private void updateAccountDetails(int accountNumber) {
        String name = view.readText("Enter your new name: ");
        String password = view.readText("Enter your new password: ");

        if (service.updateCustomerDetails(
                accountNumber, name, password)) {
            view.showMessage("Account details updated successfully.");
        } else {
            view.showMessage("Failed to update account details.");
        }
    }

    private void adminMenu() {
        boolean back = false;

        while (!back) {
            int choice = view.showAdminMenu();

            switch (choice) {
                case 1:
                    if (adminLogin()) {
                        adminOperations();
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    view.showMessage("Invalid option.");
            }
        }
    }

    private boolean adminLogin() {
        String username = view.readText("Enter admin username: ");
        String password = view.readText("Enter admin password: ");

        if (("admin1".equals(username) && "admin123".equals(password)) || ("admin2".equals(username) || "admin234".equals(password))) {
            view.showMessage("Admin login successful.");
            return true;
        }

        view.showMessage("Invalid admin credentials.");
        return false;
    }

    private void adminOperations() {
        boolean loggedIn = true;
        while (loggedIn) {
            int choice = view.showAdminOperationsMenu();

            switch (choice) {
                case 1:
                    view.showAccounts(service.getAllAccounts());
                    break;
                case 2:
                    int accountNumber =
                            view.readInt("Enter account number to search: ");
                    view.showAccount(service.searchAccount(accountNumber));
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 0:
                    loggedIn = false;
                    view.showMessage("Admin logged out.");
                    break;
                default:
                    view.showMessage("Invalid option.");
            }
        }
    }
    private void deleteAccount() {
        int accountNumber = view.readInt("Enter account number to delete: ");
        Account account = service.searchAccount(accountNumber);
        if (account == null) {
            view.showMessage("Account not found.");
            return;
        }
        view.showAccount(account);
        String confirmation = view.readText("Type YES to confirm deletion: ");
        if ("YES".equals(confirmation)) {
            if (service.deleteAccount(accountNumber)) {
                view.showMessage("Account deleted successfully.");
            } else {
                view.showMessage("Account deletion failed.");
            }
        } else {
            view.showMessage("Deletion cancelled.");
        }
    }
}
