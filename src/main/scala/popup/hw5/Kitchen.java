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

        final int solution = Dijkstra.shortestPath(c, v);

        if(solution < Integer.MAX_VALUE) {
            System.out.println(solution);
        } else {
            System.out.println("impossible");
        }

        scanner.close();
    }

    private static final class Dijkstra {
        private Dijkstra() {}

        public static int shortestPath(int[] c, int v) {

            final Map<List<Integer>, Integer> times = new HashMap<>();

            final PriorityQueue<Vertex> queue = new PriorityQueue<>();

            final List<Integer> initial = new ArrayList<>();
            for(int i = 0; i < c.length; i++) {
                initial.add(i == 0 ? c[0] : 0);
            }
            queue.add(new Vertex(initial, 0));

            while(!queue.isEmpty()) {
                final Vertex vertex = queue.poll();

                if(vertex.v.get(0) == v)
                    return vertex.distance;

                if(!times.containsKey(vertex.v)) {
                    times.put(vertex.v, vertex.distance);

                    for(int i = 0; i < c.length; i++) {
                        if(vertex.v.get(i) > 0) {
                            for(int j = 0; j < c.length; j++) {
                                if(i != j) {
                                    final List<Integer> newState = new ArrayList<>(vertex.v);
                                    final int pour = Math.min(vertex.v.get(i), c[j] - vertex.v.get(j));
                                    newState.set(i, vertex.v.get(i) - pour);
                                    newState.set(j, vertex.v.get(j) + pour);

                                    queue.add(new Vertex(newState, vertex.distance + pour));
                                }
                            }
                        }
                    }
                }
            }

            return Integer.MAX_VALUE;
        }

        private static final class Vertex implements Comparable<Vertex> {
            private final List<Integer> v;
            private final int distance;

            private Vertex(List<Integer> v, int distance) {
                this.v = v;
                this.distance = distance;
            }

            @Override
            public int compareTo(Vertex that) {
                return Integer.compare(this.distance, that.distance);
            }
        }
    }
}
