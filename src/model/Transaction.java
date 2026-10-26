package model;
import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private int fromAccount;
    private int toAccount;
    private double amount;
    private String type;
    private LocalDateTime dateTime;

    // Constructor for creating a new transaction
    public Transaction(int transactionId, int fromAccount, int toAccount, double amount, String type) {
        this(transactionId, fromAccount, toAccount, amount, type, LocalDateTime.now());
    }

    // Constructor for restoring a transaction from a file
    public Transaction(int transactionId, int fromAccount, int toAccount, double amount, String type, LocalDateTime dateTime) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.dateTime = dateTime;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionId
                + ", Type: " + type
                + ", From: " + fromAccount
                + ", To: " + toAccount
                + ", Amount: " + amount
                + ", Date: " + dateTime;
    }
}
