package popup.hw5;

import java.util.*;

public class Kitchen {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        final int[] c = new int[n];
        for(int i = 0; i < n; i++) {
            c[i] = scanner.nextInt();
        }
        final int v = scanner.nextInt();

        // --

        final List<List<Integer>> set = new ArrayList<>();
        final Map<List<Integer>, Integer> reverse = new HashMap<>();

        final List<Integer> initial = new ArrayList<>(n);
        for(int i = 0; i < n; i++)
            initial.add(0);

        generateCombinations(c, 0, c[0], initial, set, reverse);

        final List<Dijkstra.Edge> edges = new ArrayList<>();
        for(List<Integer> state : set) {
            final int from = reverse.get(state);
            for(int i = 0; i < n; i++) {
                if(state.get(i) > 0) {
                    for(int j = 0; j < n; j++) {
                        if(i != j) {
                            final List<Integer> newState = new ArrayList<>(state);
                            final int pour = Math.min(state.get(i), c[j] - state.get(j));
                            newState.set(i, state.get(i) - pour);
                            newState.set(j, state.get(j) + pour);

                            edges.add(new Dijkstra.Edge(from, reverse.get(newState), pour));
                        }
                    }
                }
            }
        }

        final Dijkstra.Solution solution = Dijkstra.shortestPath(set.size(), edges, 0);

        int min = Integer.MAX_VALUE;
        for(List<Integer> value : set) {
            if(value.get(0) == v) { // Is solution
                min = Math.min(solution.distances[reverse.get(value)], min);
            }
        }

        if(min < Integer.MAX_VALUE) {
            System.out.println(min);
        } else {
            System.out.println("impossible");
        }

        scanner.close();
    }

    private static void generateCombinations(int[] c, int i, int left, List<Integer> current, List<List<Integer>> set, Map<List<Integer>, Integer> reverse) {
        if(i < c.length) {
            for(int q = Math.min(c[i], left); q >= 0; q--) {
                final List<Integer> next = new ArrayList<>(current);
                next.set(i, q);
                generateCombinations(c, i + 1, left - q, next, set, reverse);
            }
        } else {
            if(left == 0) {
                reverse.put(current, set.size());
                set.add(current);
            }
        }
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
        public static Solution shortestPath(int n, List<Edge> edges, int s) {
            // Generalized Dijkstra with a priority queue

            final List<List<Edge>> adjacency = new ArrayList<>(n);
            for(int i = 0; i < n; i++)
                adjacency.add(new ArrayList<>());

            for(Edge edge : edges)
                adjacency.get(edge.u).add(edge);

            final int[] times = new int[n], parents = new int[n];
            for(int i = 0; i < n; i++) {
                times[i] = Integer.MAX_VALUE;
                parents[i] = -1;
            }

            final PriorityQueue<Vertex> queue = new PriorityQueue<>();

            queue.add(new Vertex(s, -1, 0));

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
            public final int t0, p;
            public final int weight;

            /**
             * Constructor for an timed-constrained, weighted directed edge.
             * For every natural k, the node can be traversed at time <code>t = t0 + p * k</code>,
             * with a time cost d.
             * @param u the source vertex
             * @param v the destination vertex
             * @param t0 the time at which this edge opens
             * @param p the modular window size
             * @param d the time cost to traverse this edge
             */
            public Edge(int u, int v, int t0, int p, int d) {
                this.u = u;
                this.v = v;
                this.t0 = t0;
                this.p = Math.abs(p);
                this.weight = d;
            }

            /**
             * Constructor for a regular weighted directed edge.
             * @param u the source vertex
             * @param v the destination vertex
             * @param weight the weight
             */
            public Edge(int u, int v, int weight) {
                this(u, v, 0, 1, weight);
            }

            /**
             * Computes the closest available time slot, according to the constraints.
             * @param t the current time
             * @return the closest time if it exists or <code>Integer.MAX_VALUE</code> otherwise
             */
            private int nextAvailable(int t) {
                if(p == 0)
                    return t <= t0 ? t0 : Integer.MAX_VALUE;
                else {
                    final int max = Math.max(t0, t);
                    return max + (p - ((max - t0) % p)) % p;
                }
            }
        }
    }
}
