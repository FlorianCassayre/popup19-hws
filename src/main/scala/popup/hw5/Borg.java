package popup.hw5;

import java.util.*;

public class Borg {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        for(int c = 0; c < n; c++) {
            final int xs = scanner.nextInt(), ys = scanner.nextInt();
            scanner.nextLine();

            final int nVertices = xs * ys;
            final List<Dijkstra.Edge> edges = new ArrayList<>();

            final List<Integer> aliens = new ArrayList<>();
            int source = -1;
            boolean[] previous = null;
            for(int y = 0; y < ys; y++) {
                final String line = scanner.nextLine();
                boolean[] current = new boolean[xs];
                for(int x = 0; x < xs; x++) {
                    final int i = y * xs + x;
                    final char ch = line.charAt(x);
                    current[x] = ch != '#';
                    if(ch == 'A')
                        aliens.add(i);
                    else if(ch == 'S')
                        source = i;

                    if(y > 0 && current[x] && previous[x]) {
                        edges.add(new Dijkstra.Edge(i, i - xs));
                        edges.add(new Dijkstra.Edge(i - xs, i));
                    }
                    if(x > 0 && current[x] && current[x - 1]) {
                        edges.add(new Dijkstra.Edge(i, i - 1));
                        edges.add(new Dijkstra.Edge(i - 1, i));
                    }
                }

                previous = current;
            }

            final List<MinimumSpanningTree.Edge> treeEdges = new ArrayList<>();
            for(int i = 0; i < aliens.size() + 1; i++) {
                final int from = i == 0 ? source : aliens.get(i - 1);
                final Dijkstra.Solution solution = Dijkstra.shortestPath(nVertices, edges, from);
                for(int j = i; j < aliens.size(); j++) {
                    final int to = aliens.get(j);
                    final int distance = solution.distances[to];
                    treeEdges.add(new MinimumSpanningTree.Edge(i, j + 1, distance));
                }
            }

            final MinimumSpanningTree.Solution solution = MinimumSpanningTree.mst(aliens.size() + 1, treeEdges);

            System.out.println(solution.weight);
        }

        scanner.close();
    }

    private static final class MinimumSpanningTree {
        private MinimumSpanningTree() {}

        /**
         * Computes the minimum spanning tree of an undirected graph.
         * @param n the number of vertices
         * @param edges the undirected edges
         * @return the corresponding tree and its total weight if the solution exists,
         * otherwise <code>null</code>. Edges are sorted in lexicographical order.
         */
        public static Solution mst(int n, List<Edge> edges) {
            // Prim's algorithm with priority queue

            final List<List<Edge>> adjacency = new ArrayList<>(n);
            for(int i = 0; i < n; i++)
                adjacency.add(new ArrayList<>());

            for(Edge edge : edges) {
                adjacency.get(edge.u).add(edge);
                adjacency.get(edge.v).add(edge);
            }

            final BitSet visited = new BitSet(n);
            final List<Edge> tree = new ArrayList<>(n - 1);

            final PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

            final int initial = 0;
            visited.set(initial);
            queue.addAll(adjacency.get(initial));

            int weight = 0;

            while(!queue.isEmpty()) { // While there are still potential vertices to connect
                final Edge e = queue.poll(); // The edge which vertex is the closest to that tree

                final int v; // The vertex outside the tree
                if(!visited.get(e.v))
                    v = e.v;
                else if(!visited.get(e.u))
                    v = e.u;
                else
                    continue; // Would create a cycle

                tree.add(e);
                weight += e.weight;
                visited.set(v);

                queue.addAll(adjacency.get(v)); // Update the queue by adding new vertices that can be connected
            }

            return tree.size() == n - 1 ? new Solution(tree, weight) : null; // A spanning tree is guaranteed to have n-1 edges
        }

        public static final class Solution {
            public final List<Edge> edges;
            public final int weight;

            private Solution(List<Edge> edges, int weight) {
                Collections.sort(edges);
                this.edges = Collections.unmodifiableList(edges);
                this.weight = weight;
            }
        }

        public static final class Edge implements Comparable<Edge> {
            public final int u, v;
            public final int weight;

            public Edge(int u, int v, int weight) {
                this.u = Math.min(u, v);
                this.v = Math.max(u, v);
                this.weight = weight;
            }

            @Override
            public int compareTo(Edge that) {
                if(this.u != that.u)
                    return Integer.compare(this.u, that.u);
                else
                    return Integer.compare(this.v, that.v);
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

            public Edge(int u, int v) {
                this(u, v, 0, 1, 1);
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
