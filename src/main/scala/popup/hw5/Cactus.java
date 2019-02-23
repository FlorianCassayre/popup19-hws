package popup.hw5;

import java.util.*;

public class Cactus {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        if(isCactus(scanner))
            System.out.println("YES");
        else
            System.out.println("NO");

        scanner.close();
    }

    private static boolean isCactus(Scanner scanner) {
        final int n = scanner.nextInt(), m = scanner.nextInt();

        final List<EulerianPath.Edge> edges = new ArrayList<>(m);
        for(int i = 0; i < m; i++) {
            final int u = scanner.nextInt(), v = scanner.nextInt();
            edges.add(new EulerianPath.Edge(u, v));
        }

        List<Integer> path = EulerianPath.findPath(n, edges);

        if(path == null) // Not Eulerian
            return false;

        if(path.isEmpty())
            return n == 1;

        if(!path.get(0).equals(path.get(path.size() - 1))) // Not Hamiltonian
            return false;

        final BitSet visited = new BitSet(n);
        final int[] index = new int[n];
        final int[] stack = new int[n];
        int height = 0;

        int reached = 0;
        for(Integer i : path) {
            if(visited.get(i)) {
                final int stackIndex = index[i];
                if(index[i] >= 0) {
                    for(int j = stackIndex + 1; j < height; j++)
                        index[stack[j]] = -1;
                    height = stackIndex + 1;
                } else { // Not in the stack: not a simple loop
                    return false;
                }
            } else {
                visited.set(i);
                index[i] = height;
                stack[height] = i;
                height++;
                reached++;
            }
        }

        return reached == n;
    }

    private static final class EulerianPath {
        private EulerianPath() {}

        /**
         * Computes a Eulerian path of a graph if it exists.
         * @param n the number of vertices
         * @param edges the edges of this graph
         * @return the order in which the vertices can be visited, or <code>null</code>
         */
        public static List<Integer> findPath(int n, List<Edge> edges) {
            if(edges.isEmpty())
                return Collections.emptyList();

            final int[] degree = new int[n];

            final List<ArrayDeque<Integer>> adjacency = new ArrayList<>(n);
            for(int i = 0; i < n; i++)
                adjacency.add(new ArrayDeque<>());
            for(Edge edge : edges) {
                adjacency.get(edge.u).add(edge.v);
                degree[edge.u]--;
                degree[edge.v]++;
            }

            int pos = -1, neg = -1;
            for(int i = 0; i < n; i++) { // Check the degree of each vertex
                final int deg = degree[i];
                if(deg != 0) {
                    if(deg == 1 && pos == -1)
                        pos = i;
                    else if(deg == -1 && neg == -1)
                        neg = i;
                    else
                        return null;
                }
            }

            boolean notHamiltonian = false;
            if(pos != -1 && neg != -1) { // The special case where the path is not Hamiltonian (but still Eulerian)
                adjacency.get(pos).add(neg);
                notHamiltonian = true;
            } else if(pos != -1 || neg != -1) {
                return null;
            }

            final Deque<Integer> exploring = new ArrayDeque<>();
            final List<Integer> history = new ArrayList<>();

            exploring.push(edges.get(0).u);

            while(!exploring.isEmpty()) {
                while(!adjacency.get(exploring.peek()).isEmpty()) {
                    final int u = exploring.peek();
                    final int v = adjacency.get(u).pop();
                    exploring.push(v);
                }

                while(!exploring.isEmpty() && adjacency.get(exploring.peek()).isEmpty()) {
                    history.add(exploring.pop());
                }
            }

            final List<Integer> path;

            if(notHamiltonian) {
                Collections.reverse(history);
                final List<Integer> old = new ArrayList<>(history);
                path = new ArrayList<>(old.size() - 1);
                int index = -1;
                for(int i = 0; i < old.size(); i++) {
                    if(old.get(i) == pos && old.get(i < old.size() - 1 ? i + 1 : 0) == neg) {
                        index = i;
                        break;
                    }
                }
                if(index == -1)
                    return null;
                for(int i = index + 1; i < old.size() - 1; i++)
                    path.add(old.get(i));
                for(int i = 0; i <= index; i++)
                    path.add(old.get(i));
            } else {
                path = new ArrayList<>(history);
            }

            return path.size() == edges.size() + 1 ? path : null;
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
