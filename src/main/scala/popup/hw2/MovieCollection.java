package popup.hw2;

import java.util.Scanner;

public class MovieCollection {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int c = scanner.nextInt();

        for(int i = 0; i < c; i++) {
            final int m = scanner.nextInt(), r = scanner.nextInt();
            final int[] indices = new int[m];
            final BinaryIndexedTree tree = new BinaryIndexedTree(m + r);
            for(int j = 0; j < m; j++) {
                indices[j] = r + j;
                tree.add(r + j, 1);
            }
            int top = r - 1;
            for(int j = 0; j < r; j++) {
                final int query = scanner.nextInt() - 1;
                final int index = indices[query];
                final long sum = tree.sum(index);

                tree.add(index, -1);

                tree.add(top, 1);
                indices[query] = top;

                top--;

                System.out.print(sum);
                if(j < r - 1)
                    System.out.print(" ");
                else
                    System.out.println();
            }
        }

        scanner.close();
    }

    private static final class BinaryIndexedTree {

        private final long[] array;

        /**
         * Creates a new Fenwick tree (indexed binary tree) data structure.
         * @param n the size of the array
         */
        public BinaryIndexedTree(int n) {
            this.array = new long[n];
        }

        /**
         * Adds delta to a[i].
         * @param i the index in the array
         * @param delta the number to add
         */
        public void add(int i, long delta) {
            if(i < 0 || i >= array.length)
                throw new IndexOutOfBoundsException(String.valueOf(i));

            i++;
            while(i <= array.length) {
                array[i - 1] += delta;
                i += Integer.lowestOneBit(i);
            }
        }

        /**
         * Computes the prefix sum a[0] + a[1] + ... + a[end - 1].
         * If the index is zero, the result is zero.
         * @param end the end index (exclusive)
         * @return the computed prefix sum
         */
        public long sum(int end) {
            if(end < 0 || end > array.length)
                throw new IndexOutOfBoundsException(String.valueOf(end));

            long sum = 0;
            while(end > 0) {
                sum += array[end - 1];
                end -= Integer.lowestOneBit(end);
            }

            return sum;
        }
    }
}
