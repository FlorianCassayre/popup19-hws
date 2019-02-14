package popup.hw4;

import java.util.Arrays;
import java.util.Scanner;

public class Uxuhul {
    private static final int ISSUES = 3;
    private static final int COMBINATIONS = 1 << ISSUES;
    
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();

        for(int i = 0; i < n; i++) {
            final int m = scanner.nextInt();

            final int[][] preferences = new int[m][COMBINATIONS];
            for(int j = 0; j < m; j++) {
                for(int k = 0; k < COMBINATIONS; k++) {
                    preferences[j][k] = scanner.nextInt() - 1;
                }
            }

            int[] outcome = new int[COMBINATIONS];
            for(int j = 0; j < COMBINATIONS; j++) // Initial outcomes: identity
                outcome[j] = j;

            for(int j = m - 1; j >= 0; j--) {
                final int[] prefs = preferences[j];
                final int[] next = new int[COMBINATIONS];

                for(int k = 0; k < COMBINATIONS; k++) {
                    int best = -1;
                    for(int b = 0; b < ISSUES; b++) {
                        final int move = k ^ (1 << b), currentOutcome = outcome[move];
                        if(best == -1 || prefs[currentOutcome] < prefs[best]) {
                            best = currentOutcome ;
                        }
                    }
                    next[k] = best;
                }

                outcome = next;
            }

            final int best = outcome[0]; // Initial setup is 000

            for(int j = ISSUES - 1; j >= 0; j--) {
                System.out.print((((best >> j) & 1) == 1) ? "Y" : "N");
            }
            System.out.println();
        }

        scanner.close();
    }
}
