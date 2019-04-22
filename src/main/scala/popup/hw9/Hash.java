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

        final int[][] counts1 = new int[firstHalf + 1][mask + 1];

        List<Integer> prevs = new ArrayList<>();
        prevs.add(0);
        counts1[0][0] = 1;

        for(int i = 1; i <= firstHalf; i++) {
            List<Integer> temp = new ArrayList<>();
            for(Integer p : prevs) {
                final int q = (33 * p) & mask;
                for(int j = 1; j <= 26; j++) {
                    final int v = q ^ j;
                    if(counts1[i][v] == 0)
                        temp.add(v);
                    counts1[i][v] += counts1[i - 1][p];
                }
            }
            prevs = temp;
        }

        final ModularArithmetic group = new ModularArithmetic(mask + 1);

        final int[][] counts2 = new int[secondHalf + 1][mask + 1];
        prevs = new ArrayList<>();
        prevs.add(k);
        counts2[0][k] = 1;
        for(int i = 1; i <= secondHalf; i++) {
            List<Integer> temp = new ArrayList<>();
            for(Integer p : prevs) {
                for(int j = 1; j <= 26; j++) {
                    final int v = (int) group.divide(p ^ j, 33);
                    if(counts2[i][v] == 0)
                        temp.add(v);
                    counts2[i][v] += counts2[i - 1][p];
                }
            }
            prevs = temp;
        }

        long total = 0;
        for(int i = 0; i <= mask; i++) {
            total += counts1[firstHalf][i] * counts2[secondHalf][i];
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
         * Divides two numbers in this group, if possible.
         * In other words, multiplies <code>a</code> by the modular inverse of <code>b</code>.
         * @param a the first number
         * @param b the second number
         * @return the result of the division if it exists, or <code>-1</code> otherwise
         */
        public long divide(long a, long b) {
            final long inv = inverse(b);
            if(inv != -1)
                return (a * inv) % n;
            else
                return inv;
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
