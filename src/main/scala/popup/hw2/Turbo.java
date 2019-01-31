package popup.hw2;

import java.util.ArrayList;
import java.util.Scanner;

public class Turbo {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        final ArrayList<Integer> list = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            final int v = scanner.nextInt() - 1;
            list.add(v);
        }

        for(int i = 0; i < n; i++) {
            final int k, index, swaps;
            if(i % 2 == 0) { // Low
                k = i / 2;
                index = list.indexOf(k);
                swaps = index;
            } else { // High
                k = n - 1 - i / 2;
                index = list.indexOf(k);
                swaps = list.size() - 1 - index;
            }
            list.remove(index);

            System.out.println(swaps);
        }

        scanner.close();
    }
}
