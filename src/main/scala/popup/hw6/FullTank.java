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

            final int[][] distances = new int[n][c + 1];
            for(int i = 0; i < n; i++)
                for(int j = 0; j <= c; j++)
                    distances[i][j] = Integer.MAX_VALUE;

            final PriorityQueue<Vertex> queue = new PriorityQueue<>();

            queue.add(new Vertex(s, 0, 0));

            while(!queue.isEmpty()) {
                final Vertex vertex = queue.poll();

                if(vertex.distance < distances[vertex.v][vertex.c]) {
                    for(int i = vertex.c; i <= c; i++) {
                        distances[vertex.v][i] = vertex.distance + (i - vertex.c) * prices[vertex.v]; // FIXME?

                        for(Edge edge : adjacency.get(vertex.v)) {
                            if(edge.weight <= i) { // Enough gas
                                queue.add(new Vertex(edge.v, distances[vertex.v][i], i - edge.weight));
                            }

                        }
                    }

                }
            }

            int min = Integer.MAX_VALUE;
            for(int i = 0; i <= c; i++) {
                min = Math.min(distances[e][i], min);
            }
            return min;
        }

        private static final class Vertex implements Comparable<Vertex> {
            private final int v;
            private final int distance;
            private final int c;

            private Vertex(int v, int distance, int c) {
                this.v = v;
                this.distance = distance;
                this.c = c;
            }

            @Override
            public int compareTo(Vertex that) {
                return Integer.compare(this.distance, that.distance);
            }
        }

        public static class Edge {
            public final int u, v;
            public final int weight;

            /**
             * Constructor for a regular weighted directed edge.
             * @param u the source vertex
             * @param v the destination vertex
             * @param weight the weight
             */
            public Edge(int u, int v, int weight) {
                this.u = u;
                this.v = v;
                this.weight = weight;
            }

        }
    }
}
