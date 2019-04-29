package popup.hw10;

import java.util.*;

public class CuckooHashing {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int t = scanner.nextInt();
        for(int c = 0; c < t; c++) {
            final int m = scanner.nextInt(), n = scanner.nextInt();
            final Pair[] table = new Pair[n];

            boolean possible = true;
            for(int i = 0; i < m; i++) {
                final int h1 = scanner.nextInt(), h2 = scanner.nextInt();

                if(possible) {
                    final Pair pair = new Pair(h1, h2);

                    if(!insert(table, pair))
                        possible = false;
                }
            }

            System.out.println(possible ? "successful hashing" : "rehash necessary");
        }

        scanner.close();
    }

    private static boolean insert(Pair[] table, Pair pair) {
        for(int j = 0; j < 1000; j++) {
            if(table[pair.h1] == null) {
                table[pair.h1] = pair;
                return true;
            } else if(table[pair.h2] == null) {
                table[pair.h2] = pair;
                return true;
            } else {
                final Pair r = table[pair.h1];
                table[pair.h1] = pair;
                pair = r.swap();
            }
        }
        return false;
    }

    private static class Pair {
        public final int h1, h2;

        private Pair(int h1, int h2) {
            this.h1 = h1;
            this.h2 = h2;
        }

        private Pair swap() {
            return new Pair(h2, h1);
        }
    }
}
