package popup.hw2;

import java.util.Scanner;

public class Turbo {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        final int[] indexes = new int[n];
        final BinaryIndexedTree tree = new BinaryIndexedTree(n);
        for(int i = 0; i < n; i++) {
            final int v = scanner.nextInt() - 1;
            indexes[v] = i;
            tree.add(i, 1);
        }

        for(int i = 0; i < n; i++) {
            final boolean low = i % 2 == 0;
            final int index = indexes[low ? i / 2 : n - 1 - i / 2];
            tree.add(index, -1);

            final long sum = tree.sum(index), swaps = low ? sum : n - i - sum - 1;;

            System.out.println(swaps);
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
