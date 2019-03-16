package popup.hw6;

import java.util.*;

public class Wormholes {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int cases = scanner.nextInt();
        for(int c = 0; c < cases; c++) {
            final Point start = readPoint(scanner), end = readPoint(scanner);
            final int n = scanner.nextInt();
            final int nVertices = 2 * (n + 1);
            final Point[] vertices = new Point[nVertices];
            vertices[0] = start;
            vertices[1] = end;
            final int[][] distances = new int[nVertices][nVertices], opening = new int[nVertices][nVertices];
            for(int i = 0; i < nVertices; i++) {
                for(int j = 0; j < nVertices; j++) {
                    distances[i][j] = Integer.MAX_VALUE;
                    opening[i][j] = Integer.MIN_VALUE;
                }
            }

            for(int i = 0; i < n; i++) {
                final Point from = readPoint(scanner), to = readPoint(scanner);
                final int t = scanner.nextInt(), d = scanner.nextInt();
                final int coord = 2 * (i + 1);
                vertices[coord] = from;
                vertices[coord + 1] = to;
                distances[coord][coord + 1] = d;
                opening[coord][coord + 1] = t;
            }

            for(int i = 0; i < nVertices; i++) {
                for(int j = 0; j < nVertices; j++) {
                    final int distance = vertices[i].distance(vertices[j]);
                    if(distance < distances[i][j]) {
                        distances[i][j] = distance;
                        opening[i][j] = Integer.MIN_VALUE;
                    }
                }
            }

            final int[] solution = BellmanFord.shortestPath(nVertices, distances, opening, 0);

            System.out.println(solution[1]);
        }

        scanner.close();
    }

    private static Point readPoint(Scanner scanner) {
        return new Point(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
    }

    private static final class Point {
        public final int x, y, z;

        private Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int distance(Point that) {
            return (int) Math.ceil(Math.sqrt(sq(x - that.x) + sq(y - that.y) + sq(z - that.z)));
        }

        private int sq(int x) {
            return x * x;
        }
    }

    private static final class BellmanFord {
        private BellmanFord() {}

        public static int[] shortestPath(int n, int[][] adjacency, int[][] opening, int s) {
            final int[] distances = new int[n];

            // Initialization
            for(int i = 0; i < n; i++) {
                distances[i] = Integer.MAX_VALUE;
            }

            distances[s] = 0; // Source

            // Relaxation
            while(true) {
                boolean updated = false;
                for(int u = 0; u < n; u++) {
                    for(int v = 0; v < n; v++) {
                        if(distances[u] < Integer.MAX_VALUE) {
                            final int travelStart = Math.max(distances[u], opening[u][v]);
                            final int weight = adjacency[u][v];
                            if(travelStart + weight < distances[v]) {
                                distances[v] = distances[u] + weight;
                                updated = true;
                            }
                        }
                    }
                }
                if(!updated)
                    break;
            }

            return distances;
        }
    }
}
