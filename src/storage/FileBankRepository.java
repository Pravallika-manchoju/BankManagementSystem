package storage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import model.Account;
import model.Transaction;
import repository.BankRepository;

public class FileBankRepository implements BankRepository {

    private final Path dataDirectory = Paths.get("data");

    private final Path accountsFile =
            dataDirectory.resolve("accounts.json");

    private final Path transactionsFile =
            dataDirectory.resolve("transactions.json");

    // Configure Gson to handle LocalDateTime.
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,new TypeAdapter<LocalDateTime>() {
                        @Override
                        public void write(JsonWriter out,LocalDateTime value) throws IOException {
                            if (value == null) {
                                out.nullValue();
                            } else {
                                out.value(value.toString());
                            }
                        }

                        @Override
                        public LocalDateTime read(JsonReader in) throws IOException {
                            if (in.peek() == JsonToken.NULL) {
                                in.nextNull();
                                return null;
                            }
                            return LocalDateTime.parse(in.nextString());
                        }
                    }).setPrettyPrinting().create();

    private final Type accountListType =
            new TypeToken<List<Account>>() {}.getType();

    private final Type transactionListType =
            new TypeToken<List<Transaction>>() {}.getType();

    // Constructor
    public FileBankRepository() {
        initializeFiles();
    }

    // Create the directory and JSON files if necessary.
    private void initializeFiles() {
        try {
            Files.createDirectories(dataDirectory);

            if (Files.notExists(accountsFile)) {
                Files.writeString(
                        accountsFile,
                        "[]",
                        StandardCharsets.UTF_8);
            }

            if (Files.notExists(transactionsFile)) {
                Files.writeString(
                        transactionsFile,
                        "[]",
                        StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize JSON data files.", e);
        }
    }

    // Read all accounts from accounts.json.
    private List<Account> readAccounts() {
        try {
            String json = Files.readString(
                    accountsFile, StandardCharsets.UTF_8);

            List<Account> accounts =
                    gson.fromJson(json, accountListType);

            return accounts == null
                    ? new ArrayList<>()
                    : accounts;

        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException(
                    "Could not read accounts.json", e);
        }
    }

    // Write all accounts to accounts.json.
    private void writeAccounts(List<Account> accounts) {
        String json = gson.toJson(accounts, accountListType);

        try {
            Files.writeString(
                    accountsFile,
                    json,
                    StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not save accounts.json", e);
        }
    }

    // Create a new account.
    @Override
    public boolean createAccount(Account account) {
        List<Account> accounts = readAccounts();

        for (Account existing : accounts) {
            if (existing.getAccountNumber()
                    == account.getAccountNumber()) {
                return false;
            }
        }

        accounts.add(account);
        writeAccounts(accounts);

        return true;
    }

    // Find an account using its account number.
    @Override
    public Account findAccount(int accountNumber) {
        for (Account account : readAccounts()) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }

        return null;
    }

    // Return all accounts.
    @Override
    public List<Account> getAllAccounts() {
        return readAccounts();
    }

    // Update an existing account.
    @Override
    public boolean updateAccount(Account account) {
        List<Account> accounts = readAccounts();

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber()
                    == account.getAccountNumber()) {

                accounts.set(i, account);
                writeAccounts(accounts);

                return true;
            }
        }

        return false;
    }

    // Delete an account.
    @Override
    public boolean deleteAccount(int accountNumber) {
        List<Account> accounts = readAccounts();

        boolean removed = accounts.removeIf(
                account -> account.getAccountNumber()
                        == accountNumber);

        if (removed) {
            writeAccounts(accounts);
        }

        return removed;
    }

    // Read all transactions from transactions.json.
    private List<Transaction> readTransactions() {
        try {
            String json = Files.readString(
                    transactionsFile, StandardCharsets.UTF_8);

            List<Transaction> transactions =
                    gson.fromJson(json, transactionListType);

            return transactions == null
                    ? new ArrayList<>()
                    : transactions;

        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException(
                    "Could not read transactions.json", e);
        }
    }

    // Write all transactions to transactions.json.
    private void writeTransactions(List<Transaction> transactions) {
        String json = gson.toJson(
                transactions, transactionListType);

        try {
            Files.writeString(
                    transactionsFile,
                    json,
                    StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not save transactions.json", e);
        }
    }

    // Save a transaction.
    @Override
    public boolean saveTransaction(Transaction transaction) {
        List<Transaction> transactions = readTransactions();

        transactions.add(transaction);
        writeTransactions(transactions);

        return true;
    }

    // Get transactions involving a particular account.
    @Override
    public List<Transaction> getTransactionHistory(
            int accountNumber) {

        List<Transaction> history = new ArrayList<>();

        for (Transaction transaction : readTransactions()) {
            if (transaction.getFromAccount() == accountNumber
                    || transaction.getToAccount() == accountNumber) {

                history.add(transaction);
            }
        }

        return history;
    }
}
