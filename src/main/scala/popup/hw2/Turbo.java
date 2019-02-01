package popup.hw2;

import java.util.Scanner;

public class Turbo {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        final int[] indexes = new int[n];
        final BinaryIndexedTree left = new BinaryIndexedTree(n), right = new BinaryIndexedTree(n);
        for(int i = 0; i < n; i++) {
            final int v = scanner.nextInt() - 1;
            indexes[v] = i;
            left.add(i, 1);
            right.add(i, 1);
        }

        for(int i = 0; i < n; i++) {
            final int index;
            final long swaps;
            if(i % 2 == 0) { // Low
                index = indexes[i / 2];
                swaps = left.sum(index);
                left.add(index, -1);
                right.add(n - 1 - index, -1);
            } else { // High
                index = n - 1 - indexes[n - 1 - i / 2];
                swaps = right.sum(index);
                right.add(index, -1);
                left.add(n - 1 - index, -1);
            }
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
