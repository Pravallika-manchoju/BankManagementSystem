package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Account;
import model.Transaction;
import repository.BankRepository;

public class PostgreSQLBankRepository implements BankRepository {

    private final String url = System.getenv().getOrDefault("BANK_DB_URL","jdbc:postgresql://localhost:5432/bank_management_db");

    private final String username = System.getenv().getOrDefault("BANK_DB_USER", "postgres");

    private final String password = System.getenv("BANK_DB_PASSWORD");

    private Connection getConnection() throws SQLException {
        if (password == null || password.isBlank()) {
            throw new SQLException("BANK_DB_PASSWORD environment variable is missing.");
        }
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts " + "(account_number, name, password, balance) " + "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, account.getAccountNumber());
            ps.setString(2, account.getName());
            ps.setString(3, account.getPassword());
            ps.setDouble(4, account.getBalance());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return false;
            }
            throw databaseError("create account", e);
        }
    }

    @Override
    public Account findAccount(int accountNumber) {
        String sql = "SELECT account_number, name, password, balance " + "FROM accounts WHERE account_number = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw databaseError("find account", e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        String sql = "SELECT account_number, name, password, balance " + "FROM accounts ORDER BY account_number";
        List<Account> accounts = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapAccount(rs));
            }
            return accounts;
        } catch (SQLException e) {
            throw databaseError("get all accounts", e);
        }
    }

    @Override
    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts " + "SET name = ?, password = ?, balance = ? " + "WHERE account_number = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, account.getName());
            ps.setString(2, account.getPassword());
            ps.setDouble(3, account.getBalance());
            ps.setInt(4, account.getAccountNumber());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw databaseError("update account", e);
        }
    }

    @Override
    public boolean deleteAccount(int accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountNumber);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw databaseError("delete account", e);
        }
    }

    @Override
    public boolean saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions "
                + "(from_account, to_account, amount, type, date_time) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
        	ps.setInt(1, transaction.getFromAccount());
        	ps.setInt(2, transaction.getToAccount());
        	ps.setDouble(3, transaction.getAmount());
        	ps.setString(4, transaction.getType());
        	ps.setTimestamp(5, Timestamp.valueOf(transaction.getDateTime()));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw databaseError("save transaction", e);
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(int accountNumber) {
        String sql = "SELECT transaction_id, from_account, to_account, " + "amount, type, date_time FROM transactions " + "WHERE from_account = ? OR to_account = ? " + "ORDER BY date_time, transaction_id";
        List<Transaction> history = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountNumber);
            ps.setInt(2, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date_time");
                    Transaction transaction = new Transaction(rs.getInt("transaction_id"),rs.getInt("from_account"),rs.getInt("to_account"),rs.getDouble("amount"),rs.getString("type"),timestamp.toLocalDateTime());
                    history.add(transaction);
                }
            }
            return history;
        } catch (SQLException e) {
            throw databaseError("get transaction history", e);
        }
    }

    private Account mapAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("account_number"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getDouble("balance"));
    }

    private IllegalStateException databaseError(String operation, SQLException e) {
        return new IllegalStateException("Failed to " + operation + ": " + e.getMessage(), e);
    }
}
