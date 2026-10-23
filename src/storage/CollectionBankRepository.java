package storage;
import java.util.*;
import model.Account;
import model.Transaction;
import repository.BankRepository;

public class CollectionBankRepository implements BankRepository {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();
    @Override
    public boolean createAccount(Account account) {
        int accountNumber = account.getAccountNumber();
        if (accounts.containsKey(accountNumber)) {
            return false;
        }
        accounts.put(accountNumber, account);
        return true;
    }
    @Override
    public Account findAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }
    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public boolean updateAccount(Account account) {
        int accountNumber = account.getAccountNumber();
        if (!accounts.containsKey(accountNumber)) {
            return false;
        }
        accounts.put(accountNumber, account);
        return true;
    }
    
    @Override
    public boolean deleteAccount(int accountNumber) {
        return accounts.remove(accountNumber) != null;
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        transactions.add(transaction);
        return true;
    }

    @Override
    public List<Transaction> getTransactionHistory(int accountNumber) {

        List<Transaction> history = new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (transaction.getFromAccount() == accountNumber || transaction.getToAccount() == accountNumber) {

                history.add(transaction);
            }
        }

        return history;
    }
}