package popup.hw7;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class BurrowsWheeler {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextLine()) {
            final String string = scanner.nextLine();

            final SuffixArray array = new SuffixArray(string);

            for(int i = 0; i < string.length(); i++)
                System.out.print(string.charAt((array.getSuffix(i) + string.length() - 1) % string.length()));
            System.out.println();
        }

        scanner.close();
    }

    private static final class SuffixArray {
        private final int[] array;

        /**
         * Constructs the suffix array for that string.
         * @param string the string
         */
        public SuffixArray(String string) {
            final int n = string.length();

            this.array = new int[n];

            final int[][] p = new int[log(n) + 1][n];
            final Tuple[] tuples = new Tuple[n];

            final Comparator<Tuple> comparator = (a, b) -> {
                if(a.first != b.first)
                    return Integer.compare(a.first, b.first);
                else if(a.second != b.second)
                    return Integer.compare(a.second, b.second);
                else
                    return Integer.compare(a.index, b.index);
            };

            for(int i = 0; i < n; i++) {
                p[0][i] = string.charAt(i);
                tuples[i] = new Tuple();
            }

            int step = 1;

            int pow = 1;
            for(int i = 0; pow < n; i++, step++) {
                for(int j = 0; j < n; j++) {
                    final Tuple tuple = tuples[j];
                    tuple.index = j;
                    tuple.first = p[i][j];
                    tuple.second = p[i][(j + pow) % n];
                }

                Arrays.sort(tuples, comparator);

                for(int j = 0; j < n; j++) {
                    p[i + 1][tuples[j].index] =
                            ((j > 0 && tuples[j].first == tuples[j - 1].first && tuples[j].second == tuples[j - 1].second)
                                    ? p[i + 1][tuples[j - 1].index]
                                    : j);
                }

                pow <<= 1;
            }

            step--;

            if(n > 1)
                for(int i = 0; i < n; i++)
                    array[p[step][i]] = i;
        }

        private static int log(int n) {
            int i = 0;
            while(1 << i < n)
                i++;
            return i;
        }

        /**
         * Gets the index of the i-th suffix in the array.
         * @param i the original index
         * @return the transformed index
         */
        public int getSuffix(int i) {
            return array[i];
        }

        private final class Tuple {
            private int first, second, index;
        }
    }

}
