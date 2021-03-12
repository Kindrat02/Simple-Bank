package bank;

import java.util.Scanner;

public class Bank {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountManager manager;

    public Bank(String databasePath) {
        manager = new AccountManager(databasePath);
    }

    public void run() {
        Account userAccount;
        while (true) {
            printStartMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    CardGenerator.setManager(manager);
                    String[] parameters = CardGenerator.generateAccountParam();
                    userAccount = new Account(parameters[0], parameters[1]);
                    System.out.println("Your card has been created");
                    printAccountInfo(userAccount);
                    manager.addAccount(userAccount);
                    break;
                case 2:
                    try {
                        userAccount = logIntoAccount();
                        runAccountMenu(userAccount);
                    } catch (Exception ex) {
                        System.out.println("Wrong card number or PIN!");
                    }
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("You`ve entered non existing point of menu");
            }
        }
    }

    private Account logIntoAccount() throws Exception {
        System.out.println("Enter your card number: ");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN: ");
        var pin = scanner.nextLine();

        return manager.getAccount(cardNumber, pin);
    }

    private void runAccountMenu(Account account) {
        System.out.println("You have successfully logged in!");
        while (true) {
            printAccountMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.println(account.getBalance());
                    break;
                case 2:
                    addIncome(account);
                    break;
                case 3:
                    doTransfer(account);
                    break;
                case 4:
                    manager.closeAccount(account);
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("You`ve entered non existing point of menu");
            }
        }
    }

    private void doTransfer(Account account) {
        System.out.println("Transfer\nEnter card number:");
        String payeeCardNumber = scanner.nextLine();
        try {
            manager.checkCardBeforeTransfer(account, payeeCardNumber);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter how much money you want to transfer: ");
        int sum = Integer.parseInt(scanner.nextLine());
        manager.doTransfer(account, payeeCardNumber, sum);
    }

    private void addIncome(Account account) {
        System.out.println("Enter income: ");
        int income = Integer.parseInt(scanner.nextLine());
        manager.addIncome(account, income);
    }

    private void exit() {
        System.out.println("Bye!");
        manager.closeConnection();
    }

    private void printStartMenu() {
        String[] starterMenu =  {"1. Create an account",
                "2. Log into account",
                "0. Exit"};

        for (String x: starterMenu) {
            System.out.println(x);
        }
    }

    private void printAccountMenu() {
        String[] accountMenu = {"1. Balance",
                "2. Add income",
                "3. Do transfer",
                "4. Close account",
                "5. Log out",
                "0. Exit"};
        for (String x: accountMenu) {
            System.out.println(x);
        }
    }

    private void printAccountInfo(Account account) {
        System.out.println("Your card number:\n" + account.getCardNumber());
        System.out.println("Your card PIN:\n" + account.getPin());
    }
}
