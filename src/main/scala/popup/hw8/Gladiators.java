package popup.hw8;

import java.math.BigInteger;
import java.util.Scanner;

public class Gladiators {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final BigInteger[][] cache = new BigInteger[100][101]; // [m][k]

        final int n = scanner.nextInt();
        for(int i = 0; i < n; i++) {
            final int m = scanner.nextInt() - 1, k = scanner.nextInt();

            System.out.println(recurrence(m, k, cache));
        }

        scanner.close();
    }

    private static BigInteger recurrence(int m, int k, BigInteger[][] cache) {
        if(cache[m][k] != null)
            return cache[m][k];

        final BigInteger value;

        if(k == 0)
            value = BigInteger.ZERO;
        else if(k == 1)
            value = BigInteger.ONE;
        else if(m == 0)
            value = BigInteger.ZERO;
        else
            value = BigInteger.valueOf(k).multiply(recurrence(m - 1, k, cache))
                    .add(BigInteger.valueOf(2 * (m + 1) - k).multiply(recurrence(m - 1, k - 1, cache)));

        cache[m][k] = value;
        return value;
    }
}
