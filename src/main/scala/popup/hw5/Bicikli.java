package popup.hw5;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Bicikli {
    private static final long DIGITS = 1_000_000_000L;
    private static final long MOD = DIGITS * 10L;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt(), m = scanner.nextInt();

        final List<BellmanFord.Edge> edges = new ArrayList<>(m);

        for(int i = 0; i < m; i++) {
            edges.add(new BellmanFord.Edge(scanner.nextInt() - 1, scanner.nextInt() - 1, 0));
        }

        final Map<BellmanFord.Edge, List<BellmanFord.Edge>> grouped = edges.stream().collect(Collectors.groupingBy(e -> e));

        final List<BellmanFord.Edge> weighted = new ArrayList<>(grouped.size());
        for(List<BellmanFord.Edge> e : grouped.values()) {
            final BellmanFord.Edge repr = e.get(0);
            weighted.add(new BellmanFord.Edge(repr.u, repr.v, e.size()));
        }

        final int start = 0, end = 1;

        final BigInteger[] solution = BellmanFord.shortestPath(n, weighted, start);

        final long count = solution[end].mod(BigInteger.valueOf(MOD)).longValue();
        if(solution[end].signum() >= 0) {
            if(count % DIGITS != count)
                System.out.println(String.format("%09d" , count % DIGITS));
            else
                System.out.println(count);
        } else
            System.out.println("inf");

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
        public static BigInteger[] shortestPath(int n, List<Edge> edges, int s) {
            BigInteger[] distances = new BigInteger[n];

            // Initialization
            for(int i = 0; i < n; i++) {
                distances[i] = BigInteger.ZERO;
            }

            distances[s] = BigInteger.ONE; // Source

            Set<Integer> positive = new HashSet<>();
            final BitSet visitedCycles = new BitSet();

            // Relaxation
            for(int i = 0; i <= n; i++) {
                BigInteger[] temp = new BigInteger[n];
                for(Edge edge : edges) {
                    final BigInteger product = distances[edge.u].multiply(BigInteger.valueOf(edge.weight));
                    temp[edge.v] = (temp[edge.v] != null ? temp[edge.v] : BigInteger.ZERO).add(product);
                }
                for(int j = 0; j < n; j++) {
                    if(i == n) { // Perform one more iteration to locate the vertices affected by negative cycles
                        if(temp[j] != null && temp[j].compareTo(distances[j]) > 0) {
                            positive.add(j);
                            visitedCycles.set(j);
                        }
                    }
                    if(temp[j] != null && temp[j].compareTo(distances[j]) > 0)
                        distances[j] = temp[j];
                }
            }

            final Map<Integer, List<Integer>> adjacency = new HashMap<>();
            for(int i = 0; i < n; i++)
                adjacency.put(i, new ArrayList<>());
            for(Edge edge : edges)
                adjacency.get(edge.u).add(edge.v);

            // Negative cycles
            while(!positive.isEmpty()) { // BFS
                final Set<Integer> temp = new HashSet<>();
                for(Integer u : positive) {
                    distances[u] = BigInteger.valueOf(-1);
                    for(Integer v : adjacency.get(u))
                        if(!visitedCycles.get(v)) {
                            temp.add(v);
                            visitedCycles.set(v);
                        }
                }
                positive = temp;
            }

            return distances;
        }

        public static final class Edge {
            public final int u, v;
            public final int weight;

            public Edge(int u, int v, int weight) {
                this.u = u;
                this.v = v;
                this.weight = weight;
            }

            @Override
            public boolean equals(Object o) {
                if(this == o)
                    return true;
                if(o == null || getClass() != o.getClass())
                    return false;
                Edge edge = (Edge) o;
                return u == edge.u && v == edge.v && weight == edge.weight;
            }

            @Override
            public int hashCode() {
                return Objects.hash(u, v, weight);
            }
        }
    }
}
