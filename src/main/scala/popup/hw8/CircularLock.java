package popup.hw8;

import java.util.Scanner;

public class CircularLock {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        for(int i = 0; i < n; i++) {
            final int s11 = scanner.nextInt(), s12 = scanner.nextInt();
            final int p11 = scanner.nextInt(), p12 = scanner.nextInt();
            final int s21 = scanner.nextInt(), s22 = scanner.nextInt();
            final int p21 = scanner.nextInt(), p22 = scanner.nextInt();

            final int p = gcd(p11, gcd(p12, gcd(p21, p22)));

            final boolean possible = ((s11 + s22) - (s12 + s21)) % p == 0;

            System.out.println(possible ? "Yes" : "No");
        }

        scanner.close();
    }

    private static int gcd(int a, int b) {
        int tmp;
        while(b != 0) {
            tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }
}
