package popup.hw2;

import java.util.Scanner;

public class KindsOfPeople {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final int r = scanner.nextInt(), c = scanner.nextInt();

        final boolean[][] map = new boolean[r][c];
        for(int i = 0; i < r; i++) {
            final String line = scanner.next();
            for(int j = 0; j < c; j++) {
                map[i][j] = line.charAt(j) == '1';
            }
        }

        final UnionFind sets = new UnionFind(r * c);
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if(i < r - 1 && map[i][j] == map[i + 1][j]) {
                    sets.union(i * c + j, (i + 1) * c + j);
                }
                if(j < c - 1 && map[i][j] == map[i][j + 1]) {
                    sets.union(i * c + j, i * c + j + 1);
                }
            }
        }

        final int n = scanner.nextInt();

        for(int i = 0; i < n; i++) {
            final int r1 = scanner.nextInt() - 1, c1 = scanner.nextInt() - 1, r2 = scanner.nextInt() - 1, c2 = scanner.nextInt() - 1;

            final int x = r1 * c + c1, y = r2 * c + c2;
            if(sets.same(x, y)) {
                System.out.println(map[r1][c1] ? "decimal" : "binary");
            } else {
                System.out.println("neither");
            }
        }

        scanner.close();
    }

    private static final class UnionFind {

        private final int[] parents; // The parent of each element (or itself if singleton)
        private final int[] ranks; // The rank of each element (initially 0)

        /**
         * Creates a new a forest of <code>n</code> singleton sets.
         * @param n the number of elements in the forest
         */
        public UnionFind(int n) {
            parents = new int[n];
            ranks = new int[n];

            for(int i = 0; i < n; i++) {
                parents[i] = i;
                ranks[i] = 0;
            }
        }

        /**
         * Merge the two sets together.
         * @param a the set containing the element a
         * @param b the set containing the element b
         */
        public void union(int a, int b) {
            checkArguments(a, b);

            int ar = find(a), br = find(b);
            if(ar != br) {
                if(ranks[ar] < ranks[br]) {
                    parents[ar] = br;
                } else {
                    parents[br] = ar;
                    if(ranks[ar] == ranks[br]) {
                        ranks[ar] += 1;
                    }
                }
            }
        }

        /**
         * Checks if the two elements are in the same set.
         * @param a the first element
         * @param b the second element
         * @return a boolean corresponding to the result
         */
        public boolean same(int a, int b) {
            checkArguments(a, b);

            return find(a) == find(b);
        }

        /**
         * Finds the parent of this element and compresses the path if necessary.
         * @param x the element to find the parent of
         * @return the parent of this element
         */
        private int find(int x) {
            if(parents[x] != x) {
                parents[x] = find(parents[x]);
            }
            return parents[x];
        }

        /**
         * Checks if the arguments are effectively elements of the forest.
         * @param a the first element
         * @param b the second element
         */
        private void checkArguments(int a, int b) {
            if(a < 0 || a >= parents.length || b < 0 || b >= parents.length)
                throw new IllegalArgumentException();
        }
    }
}
