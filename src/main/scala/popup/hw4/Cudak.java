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

        final long secondSolution = toLong(smallestSum(a, b.length, s));

        System.out.println(secondSolution);

        scanner.close();
    }

    private static int[] smallestSum(int[] a, int len, int s) {
        int[] solution = new int[len];
        for(int i = 0; i < a.length; i++)
            solution[len - a.length + i] = a[i];
        int i = 0;
        int sum;
        while((sum = sum(solution)) > s || sum + i * (TEN - 1) + (TEN - 1 - solution[len - i - 1]) < s) {
            overflow(solution, i);
            i++;
        }

        int left = s - sum;

        for(int j = 0; j <= i; j++) {
            final int take = Math.max(Math.min(left, TEN - 1), 0);
            solution[len - j - 1] += take;
            left -= take;
        }

        return solution;
    }

    private static void overflow(int[] a, int i) {
        int j = a.length - i - 1;
        a[j] = 0;
        do {
            j--;
            a[j] = (a[j] + 1) % TEN;
        } while(a[j] == 0);
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

    private static long toLong(int[] a) {
        long acc = 0;
        for(int i = 0; i < a.length; i++) {
            acc = (acc * TEN) + a[i];
        }
        return acc;
    }

    private static int sum(int[] array) {
        int sum = 0;
        for(int i = 0; i < array.length; i++)
            sum += array[i];
        return sum;
    }
}
