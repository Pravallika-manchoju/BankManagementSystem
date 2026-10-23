package model;

public class Account {
    private int accountNumber;
    private String name;
    private String password;
    private double balance;

    public Account(int accountNumber, String name, String password, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Name: " + name
                + ", Balance: " + balance;
    }
}
