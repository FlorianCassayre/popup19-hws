package popup.hw5;

import java.util.*;
import java.util.stream.Collectors;

public class Bicikli {
    private static final long DIGITS = 1_000_000_000L;
    private static final long MOD = DIGITS * 10L;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt(), m = scanner.nextInt();

        final List<Edge> dupEdges = new ArrayList<>(m);

        for(int i = 0; i < m; i++) {
            dupEdges.add(new Edge(scanner.nextInt() - 1, scanner.nextInt() - 1, 0));
        }

        final Map<Edge, List<Edge>> grouped = dupEdges.stream().collect(Collectors.groupingBy(e -> e));

        final List<Edge> weighted = new ArrayList<>(grouped.size());
        for(List<Edge> e : grouped.values()) {
            final Edge repr = e.get(0);
            weighted.add(new Edge(repr.u, repr.v, e.size()));
        }

        final List<Queue<Edge>> adjacency = new ArrayList<>(n), adjacencyOpp = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            adjacency.add(new LinkedList<>());
            adjacencyOpp.add(new LinkedList<>());
        }
        for(Edge edge : weighted) {
            adjacency.get(edge.u).add(edge);
            adjacencyOpp.get(edge.v).add(new Edge(edge.v, edge.u, edge.weight));
        }

        final int start = 0, end = 1;

        final Set<Integer> involved = bfs(start, adjacency);
        involved.retainAll(bfs(end, adjacencyOpp));

        final Queue<Integer> sorted = new ArrayDeque<>();
        final Queue<Integer> s = new ArrayDeque<>();
        final BitSet bitSet = new BitSet(n);
        final int[] incoming = new int[n];
        for(Integer u : involved) {
            if(adjacencyOpp.get(u).isEmpty()) { // No incoming edges
                s.add(u);
                bitSet.set(u);
            }
            for(Edge edge : adjacency.get(u)) {
                if(involved.contains(edge.v))
                    incoming[edge.v]++;
            }
        }

        final List<Queue<Edge>> copy = new ArrayList<>(n);
        for(int i = 0; i < n; i++)
            copy.add(new ArrayDeque<>(adjacency.get(i)));

        while(!s.isEmpty()) { // FIXME
            final int node = s.poll();
            bitSet.clear(node);
            sorted.add(node);
            while(!copy.get(node).isEmpty()) {
                final Edge edge = copy.get(node).poll();
                if(involved.contains(edge.v)) {
                    incoming[edge.v]--;
                    if(incoming[edge.v] == 0 && !bitSet.get(edge.v)) {
                        s.add(edge.v);
                        bitSet.set(edge.v);
                    }
                }
            }
        }

        boolean dag = true;
        for(int i = 0; i < incoming.length; i++) {
            if(incoming[i] != 0) {
                dag = false;
                break;
            }
        }

        final long[] distances = new long[n];
        distances[start] = 1;
        for(Integer v : sorted) {
            for(Edge edge : adjacencyOpp.get(v)) {
                final int u = edge.v;
                distances[v] = (distances[v] + distances[u] * edge.weight) % MOD;
            }
        }

        final long count = distances[end];
        if(dag) {
            if(count % DIGITS != count)
                System.out.println(String.format("%09d" , count % DIGITS));
            else
                System.out.println(count);
        } else
            System.out.println("inf");

        scanner.close();
    }

    private static Set<Integer> bfs(int start, List<Queue<Edge>> adjacency) {
        final Set<Integer> visited = new HashSet<>();
        List<Integer> current = new ArrayList<>();
        current.add(start);
        visited.add(start);
        while(!current.isEmpty()) {
            final List<Integer> temp = new ArrayList<>();
            for(Integer u : current) {
                for(Edge edge : adjacency.get(u)) {
                    if(!visited.contains(edge.v)) {
                        temp.add(edge.v);
                        visited.add(edge.v);
                    }
                }
            }
            current = temp;
        }
        return visited;
    }

    private static final class Edge {
        public final int u, v, weight;

        private Edge(int u, int v, int weight) {
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
