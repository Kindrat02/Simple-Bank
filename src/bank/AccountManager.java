package bank;

import org.sqlite.SQLiteDataSource;
import java.sql.*;

class AccountManager implements UniqueCard {

    private static Statement statement;
    private static Connection con;

    AccountManager(String databasePath) {
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl(databasePath);

        try {
            con = source.getConnection();
            statement = con.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "number TEXT, " +
                    "pin TEXT, " +
                    "balance INTEGER DEFAULT 0)");
        } catch(SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public boolean isUnique(String partOfCardNumber) {
        try {
            int set = statement.executeUpdate("SELECT * FROM card WHERE substr(number, 6) = " + partOfCardNumber);
            return set == 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        return false;
    }

    public void addAccount(Account userAccount) {
        try {
            statement.executeUpdate("INSERT INTO card(number, pin) VALUES (" + userAccount.getCardNumber() + ", " + userAccount.getPin() +")");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void checkCardBeforeTransfer(Account account, String payeeCardNumber) throws Exception {
        if (payeeCardNumber.equals(account.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");

        } else if (!checkLuhn(payeeCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again! ");

        } else if (!isCardExist(payeeCardNumber)) {
            System.out.println("Such a card does not exist.");
        }
    }

    public void doTransfer(Account account, String payeeCardNumber, int sum) {
        try {
            if (sum > account.getBalance()) {
                System.out.println("Not enough money!");
                return;
            }
            con.setAutoCommit(false);
            statement.executeUpdate("UPDATE card SET balance = balance - " + sum + " WHERE number = " + account.getCardNumber());
            statement.executeUpdate("UPDATE card SET balance = balance + " + sum + " WHERE number = " + payeeCardNumber);
            con.commit();
            con.setAutoCommit(true);
        } catch(SQLException exception) {
            exception.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    private boolean checkLuhn(String payeeCardNumber) {
        String partOfNumberCard = payeeCardNumber.substring(0, payeeCardNumber.length() - 1);
        return payeeCardNumber.equals(CardGenerator.luhnAlgorithm(partOfNumberCard));
    }

    private boolean isCardExist(String cardNumber) {
        try {
            var set = statement.executeQuery("SELECT * FROM card WHERE number = " + cardNumber + ";");
            return set.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void addIncome(Account account, int income) {
        try {
            account.replenishBalance(income);
            statement.executeUpdate("UPDATE card SET balance = balance + " + income + " WHERE number = " + account.getCardNumber() + ";");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public Account getAccount(String cardNumber, String pin) throws Exception {
        try {
            ResultSet x = statement.executeQuery("SELECT * FROM card WHERE number = " + cardNumber + " AND pin = " + pin + ";");
            x.next();
            return new Account(x.getString("number"), x.getString("pin"), x.getInt("balance"));
        } catch (SQLException ex) {
            throw new Exception();
        }
    }

    public void closeAccount(Account account) {
        try {
            statement.executeUpdate("DELETE FROM card WHERE number = " + account.getCardNumber());
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public void closeConnection() {
        try {
            statement.close();
            con.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
