package repository;

import java.util.*;
import model.Account;
import model.Transaction;

public interface BankRepository {
    boolean createAccount(Account account);

    Account findAccount(int accountNumber);

    List<Account> getAllAccounts();

    boolean updateAccount(Account account);

    boolean deleteAccount(int accountNumber);

    boolean saveTransaction(Transaction transaction);

    List<Transaction> getTransactionHistory(int accountNumber);
}