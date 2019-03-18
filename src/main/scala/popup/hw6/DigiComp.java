package popup.hw6;

import java.util.*;

public class DigiComp {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String[] firstLine = scanner.nextLine().split(" ");
        final long n = Long.parseLong(firstLine[0]);
        final int m = Integer.parseInt(firstLine[1]);

        final BitSet initial = new BitSet(m); // is_left
        final int[][] connections = new int[m + 1][2];
        for(int i = 1; i <= m; i++) {
            final String[] line = scanner.nextLine().split(" ");
            connections[i][0] = Integer.parseInt(line[1]);
            connections[i][1] = Integer.parseInt(line[2]);
            initial.set(i - 1, line[0].equals("R"));
        }

        // Topological sort
        final List<Integer> sorted = topologicalSort(connections);
        final long[] counts = new long[m + 1];
        counts[1] = n; // Base case

        for(Integer u : sorted) {
            final long count = counts[u], half = count / 2;
            counts[connections[u][0]] += half;
            counts[connections[u][1]] += half;
            if(count % 2 == 1 && u > 0) {
                counts[connections[u][initial.get(u - 1) ? 1 : 0]]++;
            }
        }

        for(int i = 0; i < m; i++) {
            final boolean state = initial.get(i) ^ (counts[i + 1] % 2 == 0);
            System.out.print(state ? "L" : "R");
        }
        System.out.println();

        scanner.close();
    }

    private static List<Integer> topologicalSort(int[][] graph) {
        BitSet visited = new BitSet();
        Deque<Integer> sorted = new ArrayDeque<>();

        for(int u = 1; u < graph.length; u++) { // Ignore the end vertex for this usage
            if(!visited.get(u)) {
                visited.set(u);
                Deque<Integer> stack = new ArrayDeque<>();
                stack.push(u);
                while(!stack.isEmpty()) {
                    int head = stack.peek();
                    boolean unvisited = false;
                    for(int v : graph[u]) {
                        if(!visited.get(v)) {
                            unvisited = true;
                            visited.set(v);
                            stack.push(v);
                        }
                    }
                    if(!unvisited) {
                        stack.pop();
                        sorted.push(head);
                    }
                }
            }
        }

        return new ArrayList<>(sorted);
    }

}
