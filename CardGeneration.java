package banking;
import java.util.Random;

public class CardGeneration {
    public static String generateCardNumber() {
        Random random = new Random();
        final long BIN = 400_000;

        long accountNumber = random.nextInt(999_999_999);
        long cardNumber = (long) (BIN * Math.pow(10, 10) + accountNumber * 10);
        int checksum = getChecksum(cardNumber);

        return Long.toString(cardNumber + checksum);

    }

    public static String generatePIN() {
        Random random = new Random();
        int PIN = random.nextInt(9999);
        return String.format("%04d", PIN);
    }

    public static int getChecksum(long cardNumber) {
        int[] numberArray = longIntoIntArray(cardNumber);
        numberArray[0] = 0;

        for (int i = 1; 2 * i - 1 < numberArray.length; i++ ) {
            numberArray[2 * i - 1] *= 2;
            if (numberArray[2 * i  - 1] >= 10) {
                numberArray[2 * i - 1] -= 9;
            }
        }

        int totalSum = 0;
        for (int j : numberArray) {
            totalSum += j;
        }
        return (int) Math.ceil(totalSum / 10.0) * 10 - totalSum;

    }

    public static int[] longIntoIntArray(long number) {
        int[] numberArray = new int[ (int) Math.log10(number) + 1];

        long tempNum = number;
        int i = 0;

        while (tempNum > 0 ) {
            numberArray[i] = (int) (tempNum % 10);
            tempNum /= 10;
            i++;
        }
        return numberArray;
    }
}




