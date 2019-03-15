package popup.hw6;

import java.util.*;

public class Gopher {
    private static final int SCALE = 10;
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        // => Max-flow problem!

        while(scanner.hasNextInt()) {

            final String[] ints = scanner.nextLine().split(" ");
            final int n = Integer.parseInt(ints[0]), m = Integer.parseInt(ints[1]), s = Integer.parseInt(ints[2]), v = Integer.parseInt(ints[3]);
            final int distanceLimitSq = sq(s * v * SCALE);

            final List<Point> sources = new ArrayList<>(n), sinks = new ArrayList<>(m);
            for(int i = 0; i < n; i++)
                sources.add(readPoint(scanner));
            for(int i = 0; i < m; i++)
                sinks.add(readPoint(scanner));

            final List<FlowSolver.Edge> edges = new ArrayList<>();
            final int source = n + m, sink = source + 1;
            for(int i = 0; i < n; i++) // Link the source to all the gophers
                edges.add(new FlowSolver.Edge(source, i, 1));
            for(int i = 0; i < n; i++) // Link all the gopher to the holes they can reach
                for(int j = 0; j < m; j++)
                    if(sources.get(i).distanceSq(sinks.get(j)) <= distanceLimitSq)
                        edges.add(new FlowSolver.Edge(i, n + j, 1));
            for(int i = 0; i < m; i++) // Link all the the holes to the sink
                edges.add(new FlowSolver.Edge(n + i, sink, 1));

            final FlowSolver.Solution solution = new FlowSolver(n + m + 2, edges, source, sink).maximumFlow();

            final long vulnerable = n - solution.f;

            System.out.println(vulnerable);
        }

        scanner.close();
    }

    private static Point readPoint(Scanner scanner) {
        final String[] line = scanner.nextLine().split(" ");
        return new Point(Math.round(Float.parseFloat(line[0]) * SCALE), Math.round(Float.parseFloat(line[1]) * SCALE));
    }

    private static int sq(int x) {
        return x * x;
    }

    private static final class Point {
        public final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int distanceSq(Point that) {
            return sq(this.x - that.x) + sq(this.y - that.y);
        }
    }

    private static class FlowSolver {

        private final int n;
        private final int s, t;
        private final long[][] capacities, flows;
        private final long[] height, excess;
        private final int[] seen;

        public FlowSolver(int n, List<Edge> edges, int s, int t) {
            this.n = n;
            this.s = s;
            this.t = t;
            this.capacities = new long[n][n];
            this.flows = new long[n][n];
            this.height = new long[n];
            this.excess = new long[n];
            this.seen = new int[n];

            for(Edge edge : edges) // Initialize capacity matrix
                capacities[edge.u][edge.v] += edge.capacity;
        }

        public Solution maximumFlow() {
            // Great inspiration of: https://en.wikipedia.org/wiki/Push%E2%80%93relabel_maximum_flow_algorithm

            final LinkedList<Integer> queue = new LinkedList<>();
            for(int i = 0; i < n; i++)
                if(i != s && i != t)
                    queue.add(i);

            height[s] = n;
            excess[s] = Long.MAX_VALUE;
            for(int v = 0; v < n; v++)
                push(s, v);

            Iterator<Integer> it = queue.iterator();
            while(it.hasNext()) {
                int u = it.next();
                long oldHeight = height[u];
                discharge(u);
                if(height[u] > oldHeight) {
                    it.remove();
                    queue.addFirst(u); // Relabel-to-front selection rule
                    it = queue.iterator();
                }
            }

            return new Solution();
        }

        private void push(int u, int v) {
            final long send = Math.min(excess[u], capacities[u][v] - flows[u][v]);
            flows[u][v] += send;
            flows[v][u] -= send;
            excess[u] -= send;
            excess[v] += send;
        }

        private void relabel(int u) {
            long min = Long.MAX_VALUE;
            for(int v = 0; v < n; v++)
                if(capacities[u][v] - flows[u][v] > 0) {
                    min = Math.min(min, height[v]);
                    if(min < Long.MAX_VALUE)
                        height[u] = min + 1;
                }
        }

        private void discharge(int u) {
            while(excess[u] > 0) {
                if(seen[u] < n) {
                    int v = seen[u];
                    if(capacities[u][v] - flows[u][v] > 0 && height[u] > height[v])
                        push(u, v);
                    else
                        seen[u]++;
                } else {
                    relabel(u);
                    seen[u] = 0;
                }
            }
        }

        public final class Solution {
            public final long f;
            public final List<Edge> flow;

            private Solution() {
                long f = 0; // Max-flow
                for(int i = 0; i < n; i++)
                    f += flows[s][i];
                this.f = f;
                this.flow = new ArrayList<>();
                for(int u = 0; u < n; u++)
                    for(int v = 0; v < n; v++)
                        if(flows[u][v] > 0)
                            this.flow.add(new Edge(u, v, flows[u][v]));
                Collections.sort(flow);
            }
        }

        public static final class Edge implements Comparable<Edge> {
            public final int u, v;
            public final long capacity;

            public Edge(int u, int v, long capacity) {
                this.u = u;
                this.v = v;
                this.capacity = capacity;
            }

            @Override
            public int compareTo(Edge that) {
                if(this.u != that.u)
                    return Integer.compare(this.u, that.u);
                else if(this.v != that.v)
                    return Integer.compare(this.v, that.v);
                else
                    return Long.compare(this.capacity, that.capacity);
            }
        }
    }
}
