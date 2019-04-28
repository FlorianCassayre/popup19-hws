package popup.hw10;

import java.util.*;

public class BigPainting {

    private static final long M = 3037000493L, B = 3, BI = 1012333498;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String[] ints = scanner.nextLine().split(" ");
        final int hp = Integer.parseInt(ints[0]), wp = Integer.parseInt(ints[1]), hm = Integer.parseInt(ints[2]), wm = Integer.parseInt(ints[3]);


        final boolean[][] pattern = new boolean[hp][wp];
        parseArray(scanner, pattern);

        final boolean[][] array = new boolean[hm][wm];
        parseArray(scanner, array);

        int id = 1;
        final Map<Long, Integer> map = new HashMap<>();
        final int[] column = new int[hp];
        for(int i = 0; i < pattern.length; i++) {
            final boolean[] pat = pattern[i];
            long hash = 0;
            long b = 1;
            for(int j = 0; j < pat.length; j++) {
                hash = (hash + (pat[j] ? 2 : 1) * b) % M;
                b = (b * B) % M;
            }

            if(!map.containsKey(hash))
                map.put(hash, id++);
            column[i] = map.get(hash);
        }

        final int[][] matches = new int[wm - wp + 1][hm];
        for(int i = 0; i < hm; i++) {
            final boolean[] arr = array[i];
            long hash = 0;
            long b = 1;
            for(int j = 0; j < wp; j++) {
                hash = (hash + (arr[j] ? 2 : 1) * b) % M;
                b = (b * B) % M;
            }
            for(int j = 0; j < matches.length; j++) {
                final Integer value = map.get(hash);
                if(value != null)
                    matches[j][i] = value;
                if(j < matches.length - 1)
                    hash = ((((hash - (arr[j] ? 2 : 1) + (arr[wp + j] ? 2 : 1) * b) * BI) % M) + M) % M;
            }
        }

        int total = 0;
        final int[] table = SubstringSearch.buildTable(column);
        for(int[] arr : matches) {
            total += SubstringSearch.find(arr, column, table).size();
        }

        System.out.println(total);

        scanner.close();
    }

    private static void parseArray(Scanner scanner, boolean[][] array) {
        final char x = 'x';

        for(int i = 0; i < array.length; i++) {
            final String s = scanner.nextLine();
            for(int j = 0; j < array[i].length; j++) {
                final boolean b = s.charAt(j) == x;
                array[i][j] = b;
            }
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
        public static List<Integer> find(int[] text, int[] pattern, int[] table) {
            // Knuth–Morris–Pratt algorithm

            final List<Integer> occurrences = new ArrayList<>();

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

        public static int[] buildTable(int[] pattern) {
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
