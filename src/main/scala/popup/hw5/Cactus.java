package popup.hw5;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class Cactus {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        if(isCactus(scanner))
            System.out.println("YES");
        else
            System.out.println("NO");

        scanner.close();
    }

    private static boolean isCactus(Scanner scanner) {
        final int n = scanner.nextInt(), m = scanner.nextInt();

        final int[] us = new int[m], vs = new int[m], verifier = new int[n];
        for(int i = 0; i < m; i++) {
            final int u = scanner.nextInt(), v = scanner.nextInt();
            us[i] = u;
            vs[i] = v;
            verifier[u]--;
            verifier[v]++;
        }

        for(int i = 0; i < n; i++)
            if(verifier[i] != 0)
                return false; // Not a simple cycle

        return reachable(n, us, vs) && reachable(n, vs, us);
    }

    private static boolean reachable(int n, int[] us, int[] vs) {
        final List<List<Integer>> adjacency = new ArrayList<>(n);
        for(int i = 0; i < n; i++)
            adjacency.add(new ArrayList<>());
        for(int i = 0; i < us.length; i++)
            adjacency.get(us[i]).add(vs[i]);

        final int s = 0;
        final BitSet reached = new BitSet();
        List<Integer> queue = new ArrayList<>();
        queue.add(0);
        reached.set(0);
        int size = 1;
        while(!queue.isEmpty()) {
            final List<Integer> temp = new ArrayList<>();
            for(Integer u : queue) {
                for(Integer v : adjacency.get(u))
                    if(!reached.get(v)) {
                        temp.add(v);
                        reached.set(v);
                        size++;
                    }
            }
            queue = temp;
        }
        return size == n;
    }
}
