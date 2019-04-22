package popup.hw9;

import java.util.*;

public class Hash {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt(), k = scanner.nextInt(), m = scanner.nextInt();

        final int mask = (1 << m) - 1;

        // ((0 ^ ord(a)) * 33 ^ ord(b)) * 33 ^ ord(c)
        // Meet in the middle

        final int firstHalf = n / 2, secondHalf = n - firstHalf;

        int[] count1 = new int[mask + 1], count2 = new int[mask + 1];
        count1[0] = 1;

        for(int i = 1; i <= firstHalf; i++) {
            int[] next = new int[mask + 1];
            for(int p = 0; p <= mask; p++) {
                if(count1[p] != 0) {
                    final int q = (33 * p) & mask;
                    for(int j = 1; j <= 26; j++) {
                        final int v = q ^ j;
                        next[v] += count1[p];
                    }
                }
            }
            count1 = next;
        }

        final ModularArithmetic group = new ModularArithmetic(mask + 1);
        final int inverse = (int) group.inverse(33);

        count2[k] = 1;
        for(int i = 1; i <= secondHalf; i++) {
            int[] next = new int[mask + 1];
            for(int p = 0; p <= mask; p++) {
                if(count2[p] != 0) {
                    for(int j = 1; j <= 26; j++) {
                        final int v = ((p ^ j) * inverse) & mask;
                        next[v] += count2[p];
                    }
                }
            }
            count2 = next;
        }

        long total = 0;
        for(int i = 0; i <= mask; i++) {
            total += count1[i] * count2[i];
        }

        System.out.println(total);

        scanner.close();
    }

    private static final class ModularArithmetic {
        public final long n;

        /**
         * Construct a new modular group of order <code>n</code>.
         * @param n the order of the group
         */
        public ModularArithmetic(long n) {
            if(n <= 0)
                throw new IllegalArgumentException();

            this.n = n;
        }

        /**
         * Computes the modular inverse of a number in this group, if possible.
         * @param a the number to invert
         * @return the modular inverse if it exists, <code>-1</code> otherwise
         */
        public long inverse(long a) {
            // Extended Euclidean algorithm

            long t = 0, nt = 1, r = n, nr = a;

            while(nr != 0) {
                final long q = r / nr, pt = nt, pr = nr;
                nt = t - q * nt;
                t = pt;
                nr = r - q * nr;
                r = pr;
            }

            if(r > 1) // Inverse does not exist
                return -1;

            if(t < 0)
                t += n;
            return t;
        }
    }
}
