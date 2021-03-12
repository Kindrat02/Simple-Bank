package bank;

public class Account {
    private int balance = 0;
    private final String pin;
    private final String cardNumber;

    public Account(String cardNumber, String pin) {
        this.pin = pin;
        this.cardNumber = cardNumber;
    }

    public Account(String cardNumber, String pin, int balance) {
        this.pin = pin;
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void replenishBalance(int sum) {
        balance += sum;
    }
}
