package popup.hw4;

import java.util.Arrays;
import java.util.Scanner;

public class Cudak {
    private static final int TEN = 10;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int[] a = toArray(scanner.nextLong()), b = toArray(scanner.nextLong() + 1);
        final int s = scanner.nextInt();


        final long[][] combinations = new long[b.length + 2][s + 1]; // [digits][sum]

        // [d][s] = sum{1 to 9}{i => [d - 1][max(s - i, 0)]}

        // O(10*d*s)
        combinations[0][0] = 1; // Base case
        for(int i = 1; i < combinations.length; i++) {
            for(int j = 0; j < combinations[i].length; j++) {
                long acc = 0;
                for(int k = 0; k < Math.min(j + 1, TEN); k++) {
                    acc += combinations[i - 1][j - k];
                }
                combinations[i][j] = acc;
            }
        }

        final long below = sumBelow(a, s, combinations), above = sumBelow(b, s, combinations);

        final long firstSolution = above - below;

        System.out.println(firstSolution);

        scanner.close();
    }

    private static long sumBelow(int[] a, int s, long[][] combinations) {
        long sum = 0;
        int sumAcc = sum(a);
        for(int i = a.length - 1; i >= 0; i--) {
            sumAcc -= a[i];
            for(int j = 0; j < a[i]; j++) {
                final int goal = s - sumAcc - j;
                if(goal >= 0)
                    sum += combinations[a.length - i - 1][goal];
            }
        }
        return sum;
    }

    private static int[] toArray(long n) {
        final String s = Long.toString(n);
        final int[] array = new int[s.length()];
        for(int i = 0; i < array.length; i++)
            array[i] = s.charAt(i) - '0';
        return array;
    }

    private static int sum(int[] array) {
        int sum = 0;
        for(int i = 0; i < array.length; i++)
            sum += array[i];
        return sum;
    }
}
