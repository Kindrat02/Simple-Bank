package bank;

import java.util.Random;

public class CardGenerator {
    private final static int NUMBER = 9;
    private static UniqueCard manager;

    public static void setManager(UniqueCard m) {
        manager = m;
    }

    public static String[] generateAccountParam() {
        Random random = new Random();

        StringBuilder part = new StringBuilder();
        part.setLength(NUMBER);
        do {
            for (int i = 0; i < part.length(); ++i) {
                part.setCharAt(i, (char) ('0' + random.nextInt(10)));
            }
        } while (!manager.isUnique(part.toString()));
        String cardNumber = "400000" + part.toString();

        StringBuilder pin = new StringBuilder();
        pin.setLength(4);
        for (int i = 0; i < pin.length(); ++i) {
            pin.setCharAt(i, (char) ('0' + random.nextInt(10)));
        }

        return new String[]{luhnAlgorithm(cardNumber), pin.toString()};
    }

    public static String luhnAlgorithm(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); ++i) {
            if (i % 2 == 0) {
                int t = ((cardNumber.charAt(i) - '0') * 2);
                sum += t > 9 ? t - 9 :t;
            } else {
                sum += (cardNumber.charAt(i) - '0');
            }
        }

        int roundSum = (sum + 9) - (sum + 9) % 10;
        return cardNumber + (roundSum - sum);
    }
}
