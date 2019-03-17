package popup.hw6;

import java.util.*;

public class FullTank {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt(), m = scanner.nextInt();
        final int[] prices = new int[n];
        for(int i = 0; i < n; i++) {
            final int p = scanner.nextInt();
            prices[i] = p;
        }
        final List<Dijkstra.Edge> edges = new ArrayList<>(m);
        for(int i = 0; i < m; i++) {
            final int u = scanner.nextInt(), v = scanner.nextInt(), d = scanner.nextInt();
            edges.add(new Dijkstra.Edge(u, v, d));
            edges.add(new Dijkstra.Edge(v, u, d));
        }
        final int q = scanner.nextInt();
        for(int i = 0; i < q; i++) {
            final int c = scanner.nextInt(), s = scanner.nextInt(), e = scanner.nextInt();

            final int solution = Dijkstra.shortestPath(n, edges, prices, c, s, e);
            System.out.println(solution < Integer.MAX_VALUE ? solution : "impossible");
        }

        scanner.close();
    }

    private static final class Dijkstra {
        private Dijkstra() {}

        /**
         * Finds the shortest paths from a single source using Dijkstra.
         * This function supports both regular edges (u, v, w),
         * and time tabled edges (u, v, t0, p, d).
         * @param n the number of vertices
         * @param edges the extended weighted directed edges
         * @param s the source vertex
         * @return the shortest distances to each target if it exists,
         * or <code>Integer.MAX_VALUE</code> otherwise; as well as the
         * parent vertex or <code>-1</code> if it is the root of the tree
         * or if there exist no shortest path to that vertex.
         */
        public static int shortestPath(int n, List<Edge> edges, int[] prices, int c, int s, int e) {

            final List<List<Edge>> adjacency = new ArrayList<>(n);
            for(int i = 0; i < n; i++)
                adjacency.add(new ArrayList<>());

            for(Edge edge : edges)
                adjacency.get(edge.u).add(edge);

            final int[][] costs = new int[n][c + 1];
            for(int i = 0; i < n; i++)
                for(int j = 0; j <= c; j++)
                    costs[i][j] = Integer.MAX_VALUE;

            final PriorityQueue<Vertex> queue = new PriorityQueue<>();

            queue.add(new Vertex(s, 0, 0));

            while(!queue.isEmpty()) {
                final Vertex vertex = queue.poll();

                if(vertex.v == e)
                    return vertex.cost;

                if(vertex.cost < costs[vertex.v][vertex.gas]) {
                        costs[vertex.v][vertex.gas] = vertex.cost;

                        if(vertex.gas < c)
                            queue.add(new Vertex(vertex.v, vertex.cost + prices[vertex.v], vertex.gas + 1));

                        for(Edge edge : adjacency.get(vertex.v)) {
                            if(edge.weight <= vertex.gas) { // Enough gas
                                queue.add(new Vertex(edge.v, vertex.cost, vertex.gas - edge.weight));
                            }
                        }
                }
            }

            return Integer.MAX_VALUE;
        }

        private static final class Vertex implements Comparable<Vertex> {
            private final int v;
            private final int cost;
            private final int gas;

            private Vertex(int v, int cost, int gas) {
                this.v = v;
                this.cost = cost;
                this.gas = gas;
            }

            @Override
            public int compareTo(Vertex that) {
                return Integer.compare(this.cost, that.cost);
            }
        }

        public static class Edge {
            public final int u, v;
            public final int weight;

            public Edge(int u, int v, int weight) {
                this.u = u;
                this.v = v;
                this.weight = weight;
            }

        }
    }
}
