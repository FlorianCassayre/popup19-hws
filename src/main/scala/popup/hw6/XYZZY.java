package popup.hw6;

import java.util.*;

public class XYZZY {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        while(true) {
            final int n = scanner.nextInt();
            if(n == -1)
                break;

            final int start = 0;

            final int[] energy = new int[n];
            final List<BellmanFord.Edge> edges = new ArrayList<>();
            final List<List<Integer>> adjacency = new ArrayList<>(n);
            for(int i = 0; i < n; i++) {
                final int e = scanner.nextInt(), edgesCount = scanner.nextInt();

                final List<Integer> adjacent = new ArrayList<>(edgesCount);

                energy[i] = -e;
                for(int j = 0; j < edgesCount; j++) {
                    final int to = scanner.nextInt() - 1;
                    edges.add(new BellmanFord.Edge(i, to));
                    adjacent.add(to);
                }

                adjacency.add(adjacent);
            }

            final boolean result = BellmanFord.shortestPath(n, edges, adjacency, energy, start);

            System.out.println(result ? "winnable" : "hopeless");
        }

        scanner.close();
    }

    private static final class BellmanFord {
        private BellmanFord() {}

        /**
         * Computes all the shortest paths from a single source.
         * Negative weights are allowed, and negative cycle will be detected.
         * @param n the number of vertices
         * @param edges the edges
         * @param s the source vertex
         * @return a solution containing the costs and the paths;
         * a distance of <code>Integer.MAX_VALUE</code> indicates an impossible path
         * while <code>Integer.MIN_VALUE</code> means that the cost of the path can be
         * made arbitrarily low. The paths are stored in a tree
         */
        public static boolean shortestPath(int n, List<Edge> edges, List<List<Integer>> adjacency, int[] energy, int s) {
            final int[] distances = new int[n];

            // Initialization
            for(int i = 0; i < n; i++) {
                distances[i] = Integer.MAX_VALUE;
            }

            distances[s] = -100; // Source

            List<Integer> negative = new ArrayList<>();
            final BitSet visitedCycles = new BitSet();

            // Relaxation
            for(int i = 0; i < n; i++) {
                for(Edge edge : edges) {
                    final int weight = energy[edge.v];
                    final int distance = distances[edge.u] + weight;
                    if(distances[edge.u] < Integer.MAX_VALUE && distance < 0 && distance < distances[edge.v]) {
                        distances[edge.v] = distance;
                        if(i == n - 1) { // Perform one more iteration to locate the vertices affected by negative cycles
                            negative.add(edge.v);
                            visitedCycles.set(edge.v);
                        }
                    }
                }
                if(distances[n - 1] < 0)
                    return true;
            }

            // Negative cycles
            while(!negative.isEmpty()) { // BFS
                final List<Integer> temp = new ArrayList<>();
                for(Integer u : negative) {
                    distances[u] = Integer.MIN_VALUE;
                    for(Integer v : adjacency.get(u))
                        if(!visitedCycles.get(v)) {
                            temp.add(v);
                            visitedCycles.set(v);
                        }
                }
                negative = temp;
            }

            return distances[n - 1] < 0;
        }

        public static final class Edge {
            public final int u, v;

            public Edge(int u, int v) {
                this.u = u;
                this.v = v;
            }
        }
    }
}
