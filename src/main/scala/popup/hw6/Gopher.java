package popup.hw6;

import java.util.*;

public class Gopher {
    private static final int SCALE = 10;
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        // => Max-flow problem!
        // ===> Maximum bipartite matching

        while(scanner.hasNextInt()) {

            final String[] ints = scanner.nextLine().split(" ");
            final int n = Integer.parseInt(ints[0]), m = Integer.parseInt(ints[1]), s = Integer.parseInt(ints[2]), v = Integer.parseInt(ints[3]);
            final int distanceLimitSq = sq(s * v * SCALE);

            final Point[] gophers = new Point[n], holes = new Point[m];
            for(int i = 0; i < n; i++)
                gophers[i] = readPoint(scanner);
            for(int i = 0; i < m; i++)
                holes[i] = readPoint(scanner);

            final boolean[][] graph = new boolean[n][m];
            for(int i = 0; i < n; i++) // Link all the gopher to the holes they can reach
                for(int j = 0; j < m; j++)
                    if(gophers[i].distanceSq(holes[j]) <= distanceLimitSq)
                        graph[i][j] = true;

            final int maxMatching = maximumBipartiteMatching(n, m, graph);

            final long vulnerable = n - maxMatching;

            System.out.println(vulnerable);
        }

        scanner.close();
    }

    private static int maximumBipartiteMatching(int n, int m, boolean[][] graph) {
        int[] assigned = new int[n];
        for(int i = 0; i < n; i++)
            assigned[i] = -1;
        int paths = 0;

        for(int u = 0; u < m; u++) {
            boolean[] visited = new boolean[n];
            for(int i = 0; i < n; i++)
                visited[i] = false;
            if(bipartiteMatch(u, visited, assigned, graph))
                paths++;
        }
        return paths;
    }

    private static boolean bipartiteMatch(int u, boolean[] visited, int[] assigned, boolean[][] graph) {
        for(int v = 0; v < graph.length; v++) {
            if(graph[u][v] && !visited[v]) {
                visited[v] = true;
                if(assigned[v] < 0 || bipartiteMatch(assigned[v], visited, assigned, graph)) {
                    assigned[v] = u;
                    return true;
                }
            }
        }
        return false;
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
}
