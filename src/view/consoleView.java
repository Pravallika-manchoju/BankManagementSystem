package view;

import java.util.*;
import model.Account;
import model.Transaction;
public class consoleView {
    private final Scanner scanner = new Scanner(System.in);
    public int showStorageMenu() {
        System.out.println("\n===== BANKING MANAGEMENT SYSTEM =====");
        System.out.println("1. Collections");
        System.out.println("2. Files");
        System.out.println("3. PostgreSQL");
        System.out.println("0. Exit");
        return readInt("Choose storage: ");
    }

    public int showRoleMenu() {
        System.out.println("\n===== SELECT USER ROLE =====");
        System.out.println("1. Customer");
        System.out.println("2. Admin");
        System.out.println("0. Exit");
        return readInt("Choose role: ");
    }

    public int showCustomerMenu() {
        System.out.println("\n===== CUSTOMER MENU =====");
        System.out.println("1. Create account");
        System.out.println("2. Login");
        System.out.println("0. Back");
        return readInt("Choose an option: ");
    }

    public int showCustomerOperationsMenu() {
        System.out.println("\n===== CUSTOMER OPERATIONS =====");
        System.out.println("1. Check balance");
        System.out.println("2. Deposit money");
        System.out.println("3. Send money");
        System.out.println("4. View transaction history");
        System.out.println("5. View account details");
        System.out.println("6. Update account details");
        System.out.println("0. Logout");
        return readInt("Choose an option: ");
    }

    public int showAdminMenu() {
        System.out.println("\n===== ADMIN MENU =====");
        System.out.println("1. Login");
        System.out.println("0. Back");
        return readInt("Choose an option: ");
    }

    public int showAdminOperationsMenu() {
        System.out.println("\n===== ADMIN OPERATIONS =====");
        System.out.println("1. View all accounts");
        System.out.println("2. Search account");
        System.out.println("3. Delete account");
        System.out.println("0. Logout");
        return readInt("Choose an option: ");
    }

    public int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (Double.isFinite(value)){
                    return value;
                }

                System.out.println("Please enter a finite number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    public String readText(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void showAccount(Account account) {
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.println("\n" + account);
    }

    public void showAccounts(List<Account> accounts) {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    public void showTransactionHistory(List<Transaction> history) {
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : history) {
            System.out.println(transaction);
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
