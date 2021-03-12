package bank;

public class Main {
    public static void main(String[] args) {
        String dbPath = "card.d3bs";

        Bank kredoBank = new Bank("jdbc:sqlite:" + dbPath);
        kredoBank.run();
    }
}
