package popup.hw2;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

public class BinarySearchTree {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();

        int c = 0;
        final int[] costs = new int[n];
        final TreeSet<Integer> tree = new TreeSet<>();

        for(int i = 0; i < n; i++) {
            final int k = scanner.nextInt() - 1;

            final Integer smaller = tree.floor(k), bigger = tree.ceiling(k);
            final int cost;

            if(smaller != null && bigger != null)
                cost = Math.max(costs[smaller], costs[bigger]);
            else if(smaller != null)
                cost = costs[smaller];
            else if(bigger != null)
                cost = costs[bigger];
            else
                cost = 0;

            costs[k] = cost + 1;
            tree.add(k);
            c += cost;

            System.out.println(c);
        }

        scanner.close();
    }
}
