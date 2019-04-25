package popup.hw10;

import java.math.BigInteger;
import java.util.*;

public class BigPainting {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String[] ints = scanner.nextLine().split(" ");
        final int hp = Integer.parseInt(ints[0]), wp = Integer.parseInt(ints[1]), hm = Integer.parseInt(ints[2]), wm = Integer.parseInt(ints[3]);


        final boolean[][] pattern = new boolean[hp][wp];
        final BigInteger[] patternBi = new BigInteger[hp];
        parseArray(scanner, pattern, patternBi);

        final boolean[][] array = new boolean[hm][wm];
        final BigInteger[] arrayBi = new BigInteger[hm];
        parseArray(scanner, array, arrayBi);

        int id = 1;
        final Map<BigInteger, Integer> map = new HashMap<>();
        final int[] column = new int[hp];
        for(int i = 0; i < patternBi.length; i++) {
            final BigInteger bi = patternBi[i];
            if(!map.containsKey(bi))
                map.put(bi, id++);
            column[i] = map.get(bi);
        }

        final BigInteger mask = BigInteger.ZERO.setBit(wp).subtract(BigInteger.ONE);

        final int[][] matches = new int[wm - wp + 1][hm];
        for(int i = 0; i < hm; i++) {
            BigInteger acc = arrayBi[i];
            for(int j = 0; j <= wm - wp; j++) {
                final BigInteger key = acc.and(mask);

                final Integer val = map.get(key);
                if(val != null)
                    matches[j][i] = val;
                acc = acc.shiftRight(1);
            }
        }

        int total = 0;
        for(int[] arr : matches) {
            total += SubstringSearch.find(arr, column).size();
        }

        System.out.println(total);

        scanner.close();
    }

    private static void parseArray(Scanner scanner, boolean[][] array, BigInteger[] arrayBi) {
        final char x = 'x';

        for(int i = 0; i < array.length; i++) {
            final String s = scanner.nextLine();
            BigInteger bi = BigInteger.ZERO;
            for(int j = 0; j < array[i].length; j++) {
                final boolean b = s.charAt(j) == x;
                array[i][j] = b;
                if(b)
                    bi = bi.setBit(j);
            }
            arrayBi[i] = bi;
        }
    }

    private static final class Matcher {
        private final boolean[][] patterns, array;

        private Matcher(boolean[][] patterns, boolean[][] array) {
            this.patterns = patterns;
            this.array = array;
        }
    }

    private static final class SubstringSearch {
        private SubstringSearch() {}

        /**
         * Efficiently finds all occurrences of a string in another string.
         * @param text the haystack
         * @param pattern the needle
         * @return all indices where the pattern starts
         */
        public static List<Integer> find(int[] text, int[] pattern) {
            // Knuth–Morris–Pratt algorithm

            final List<Integer> occurrences = new ArrayList<>();
            final int[] table = buildTable(pattern);

            int j = 0, k = 0;
            while(j < text.length) {
                if(pattern[k] == text[j]) {
                    j++;
                    k++;
                    if(k == pattern.length) {
                        occurrences.add(j - k);
                        k = table[k];
                    }
                } else {
                    k = table[k];
                    if(k < 0) {
                        j++;
                        k++;
                    }
                }
            }

            return occurrences;
        }

        private static int[] buildTable(int[] pattern) {
            // KMP requires a table of partial matches

            final int[] table = new int[pattern.length + 1];

            int pos = 1, cnd = 0;

            table[0] = -1;

            while(pos < pattern.length) {
                if(pattern[pos] == pattern[cnd])
                    table[pos] = table[cnd];
                else {
                    table[pos] = cnd;
                    cnd = table[cnd];
                    while(cnd >= 0 && pattern[pos] != pattern[cnd])
                        cnd = table[cnd];
                }
                pos++;
                cnd++;
            }
            table[pos] = cnd;

            return table;
        }
    }
}
