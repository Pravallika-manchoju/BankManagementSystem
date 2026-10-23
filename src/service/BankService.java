package service;

import java.util.List;

import model.Account;
import model.Transaction;
import repository.BankRepository;

public class BankService {
    private final BankRepository repository;
    private int nextTransactionId = 1;
    public BankService(BankRepository repository) {
        this.repository = repository;
    }
    private int generateAccountNumber() {
        int maxAccountNumber = 1000;
        for (Account account : repository.getAllAccounts()) {
            if (account.getAccountNumber() > maxAccountNumber) {
                maxAccountNumber = account.getAccountNumber();
            }
        }
        return maxAccountNumber + 1;
    }
    public Account createAccount(String name, String password,double initialBalance) {
        if (name == null || name.trim().isEmpty() || password == null || password.isEmpty() || initialBalance < 0) {
            return null;
        }
        int accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, name, password, initialBalance);
        if (repository.createAccount(account)) {
            return account;
        }
        return null;
    }
    public Account login(int accountNumber, String password) {
        Account account = repository.findAccount(accountNumber);
        if (account != null && account.getPassword().equals(password)){
            return account;
        }
        return null;
    }

    public double getBalance(int accountNumber) {
        Account account = repository.findAccount(accountNumber);
        if (account == null) {
        	throw new IllegalStateException ("Account " + accountNumber + " does not exist. Please create account first.");
        }
        return account.getBalance();
    }

    public boolean deposit(int accountNumber, double amount) {
        Account account = repository.findAccount(accountNumber);
        if (account == null ) {
            throw new IllegalStateException ("No user with account number" + accountNumber + "found");
        }
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        if (!repository.updateAccount(account)){
            return false;
        }
        Transaction transaction = new Transaction(nextTransactionId++,accountNumber,accountNumber,amount,"DEPOSIT");
        return repository.saveTransaction(transaction);
    }
    public boolean transfer(int senderAccountNumber, int receiverAccountNumber, double amount) {
        Account sender = repository.findAccount(senderAccountNumber);
        Account receiver = repository.findAccount(receiverAccountNumber);
        if (sender == null || receiver == null || senderAccountNumber == receiverAccountNumber || amount <= 0 || sender.getBalance() < amount) {
            return false;
        }
        double receiverNewBalance = receiver.getBalance() + amount;
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiverNewBalance);
        if (!repository.updateAccount(sender) || !repository.updateAccount(receiver)) {
            return false;
        }
        Transaction transaction = new Transaction(nextTransactionId++,senderAccountNumber, receiverAccountNumber, amount, "TRANSFER");
        return repository.saveTransaction(transaction);
    }
    public boolean updateCustomerDetails(int accountNumber, String newName, String newPassword) {
        Account account = repository.findAccount(accountNumber);
        if (account == null || newName == null || newName.trim().isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return false;
        }
        account.setName(newName);
        account.setPassword(newPassword);
        return repository.updateAccount(account);
    }

    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }

    public Account searchAccount(int accountNumber) {
        return repository.findAccount(accountNumber);
    }

    public boolean deleteAccount(int accountNumber) {
        return repository.deleteAccount(accountNumber);
    }

    public List<Transaction> getTransactionHistory(int accountNumber) {
        return repository.getTransactionHistory(accountNumber);
    }
}
