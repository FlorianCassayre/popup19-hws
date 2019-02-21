package popup.hw5;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class George {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt(), m = scanner.nextInt();
        final int a = scanner.nextInt() - 1, b = scanner.nextInt() - 1, k = scanner.nextInt(), g = scanner.nextInt();

        final int[] labels = new int[g];
        for(int i = 0; i < g; i++) {
            final int label = scanner.nextInt() - 1;
            labels[i] = label;
        }

        final List<Dijkstra.Edge> edges = new ArrayList<>(m);
        for(int i = 0; i < m; i++) {
            final int u = scanner.nextInt() - 1, v = scanner.nextInt() - 1, w = scanner.nextInt();
            final Dijkstra.Edge edge = new Dijkstra.Edge(u, v, 0, w);
            edges.add(edge);
        }

        final Dijkstra.Solution solution = Dijkstra.shortestPath(n, edges, labels, k, a);

        System.out.println(solution.distances[b] - k);

        scanner.close();
    }

    private static final class Dijkstra {
        private Dijkstra() {}

        /**
         * Finds the shortest paths from a single source using Dijkstra.
         * This function supports both regular edges (u, v, w),
         * and time tabled edges (u, v, t0, t1, d).
         * @param n the number of vertices
         * @param edges the extended weighted directed edges
         * @param s the source vertex
         * @return the shortest distances to each target if it exists,
         * or <code>Integer.MAX_VALUE</code> otherwise; as well as the
         * parent vertex or <code>-1</code> if it is the root of the tree
         * or if there exist no shortest path to that vertex.
         */
        public static Solution shortestPath(int n, List<Edge> edges, int[] ts, int off, int s) {
            // Generalized Dijkstra with a priority queue

            final List<List<Edge>> adjacency = new ArrayList<>(n);
            for(int i = 0; i < n; i++)
                adjacency.add(new ArrayList<>());

            for(Edge edge : edges) {
                adjacency.get(edge.u).add(edge);
                adjacency.get(edge.v).add(new Edge(edge.v, edge.u, edge.t0, edge.weight));
            }

            int acc = 0;
            for(int i = 1; i < ts.length; i++) {
                final int from = ts[i - 1], to = ts[i];
                final List<Edge> a1 = adjacency.get(from), a2 = adjacency.get(to);
                int cost = 0;
                for(int j = 0; j < a1.size(); j++)
                    if(a1.get(j).v == to) {
                        a1.get(j).t0 = acc;
                        cost = a1.get(j).weight;
                        break;
                    }
                for(int j = 0; j < a2.size(); j++)
                    if(a2.get(j).v == from) {
                        a2.get(j).t0 = acc;
                        break;
                    }

                acc += cost;
            }

            final int[] times = new int[n], parents = new int[n];
            for(int i = 0; i < n; i++) {
                times[i] = Integer.MAX_VALUE;
                parents[i] = -1;
            }

            final PriorityQueue<Vertex> queue = new PriorityQueue<>();

            queue.add(new Vertex(s, -1, off));

            while(!queue.isEmpty()) {
                final Vertex vertex = queue.poll();

                if(times[vertex.v] == Integer.MAX_VALUE) {
                    times[vertex.v] = vertex.distance;
                    parents[vertex.v] = vertex.parent;

                    for(Edge edge : adjacency.get(vertex.v)) {
                        final int next = edge.nextAvailable(vertex.distance);
                        if(next < Integer.MAX_VALUE)
                            queue.add(new Vertex(edge.v, edge.u, next + edge.weight));
                    }
                }
            }

            return new Solution(n, times, parents);
        }

        public static final class Solution {
            public final int n;
            public final int[] distances, parents;

            private Solution(int n, int[] distances, int[] parents) {
                this.n = n;
                this.distances = distances;
                this.parents = parents;
            }
        }

        private static final class Vertex implements Comparable<Vertex> {
            private final int v, parent;
            private final int distance;

            private Vertex(int v, int parent, int distance) {
                this.v = v;
                this.parent = parent;
                this.distance = distance;
            }

            @Override
            public int compareTo(Vertex that) {
                return Integer.compare(this.distance, that.distance);
            }
        }

        public static class Edge {
            public final int u, v;
            public int t0;
            public final int weight;

            /**
             * Constructor for an timed-constrained, weighted directed edge.
             * For every natural k, the node can be traversed at time <code>t = t0 + t1 * k</code>,
             * with a time cost d.
             * @param u the source vertex
             * @param v the destination vertex
             * @param t0 the time at which this edge opens
             * @param weight the time cost to traverse this edge
             */
            public Edge(int u, int v, int t0, int weight) {
                this.u = u;
                this.v = v;
                this.t0 = t0;
                this.weight = weight;
            }
            /**
             * Computes the closest available time slot, according to the constraints.
             * @param t the current time
             * @return the closest time if it exists or <code>Integer.MAX_VALUE</code> otherwise
             */
            private int nextAvailable(int t) {
                if(t < t0)
                    return t;
                else
                    return Math.max(t0 + weight, t);
            }
        }
    }
}
