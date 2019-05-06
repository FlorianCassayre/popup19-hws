package popup.hw12;

import java.util.*;

public class CrissCross {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        final LineSegment[] segments = new LineSegment[n];
        for(int i = 0; i < n; i++)
            segments[i] = readLine(scanner);

        boolean finite = true;
        final Set<LineSegment.SingleIntersection> intersections = new HashSet<>();
        for(int i = 0; i < n && finite; i++) {
            final LineSegment a = segments[i];
            for(int j = i + 1; j < n && finite; j++) {
                final LineSegment b = segments[j];

                final LineSegment.Result result = a.intersection(b);
                if(result instanceof LineSegment.SingleIntersection) {
                    intersections.add((LineSegment.SingleIntersection) result);
                } else if(result instanceof LineSegment.InfiniteIntersection) {
                    finite = false;
                }
            }
        }

        System.out.println(finite ? intersections.size() : -1);

        scanner.close();
    }

    private static LineSegment readLine(Scanner scanner) {
        return new LineSegment(readPoint(scanner), readPoint(scanner));
    }

    private static Vector readPoint(Scanner scanner) {
        return new Vector(scanner.nextInt(), scanner.nextInt());
    }

    private static class LineSegment {
        private static final Comparator<Vector> COMPARATOR = (a, b) -> {
            if(a.x != b.x)
                return Long.compare(a.x, b.x);
            else
                return Long.compare(a.y, b.y);
        };

        public final Vector a, b;
        private final long lx, ly, lz;

        public LineSegment(Vector a, Vector b) {
            final Vector[] points = {a, b};
            Arrays.sort(points, COMPARATOR);

            this.a = points[0];
            this.b = points[1];

            this.lx = b.y - a.y;
            this.ly = a.x - b.x;
            this.lz = -(lx * a.x + ly * a.y);
        }

        public boolean isPoint() {
            return a.equals(b);
        }

        public Result intersection(LineSegment that) {
            // 3D cross product
            final long cx = this.ly * that.lz - this.lz * that.ly, cy = this.lz * that.lx - this.lx * that.lz, cz = this.lx * that.ly - this.ly * that.lx;

            if(cz == 0) { // Lines are parallel or one of them is a point
                final boolean ap = this.isPoint(), bp = that.isPoint();
                final LineSegment la = ap ? that : this, lb = ap ? this : that;
                if(ap && bp) { // Both segments are points
                    if(this.a.equals(that.a))
                        return new SingleIntersection(this.a.x, this.a.y, 1);
                    else
                        return new NoIntersection();
                } else if(la.lx * lb.a.x + la.ly * lb.a.y + la.lz == 0) { // Lines are on the same axis
                    final Vector[] points = {this.a, this.b, that.a, that.b};
                    Arrays.sort(points, COMPARATOR);

                    if(points[1].equals(points[2])) {
                        return new SingleIntersection(points[1].x, points[2].y, 1); // Intersection is a single point
                    } else if(points[1] == this.b && points[2] == that.a || points[1] == that.b && points[2] == this.a) {
                        return new NoIntersection(); // Segments are disjoint
                    } else {
                        return new InfiniteIntersection(); // Intersection is a segment
                    }
                } else {
                    return new NoIntersection();
                }
            } else { // Lines are non parallel
                final double x = (double) cx / cz, y = (double) cy / cz;

                if(this.inside(x, y) && that.inside(x, y))
                    return new SingleIntersection(cx, cy, cz); // Non parallel single point intersection
                else
                    return new NoIntersection(); // Lines intersect but not the segments
            }
        }

        private boolean inside(double x, double y) {
            return x >= Math.min(a.x, b.x) && x <= Math.max(a.x, b.x) && y >= Math.min(a.y, b.y) && y <= Math.max(a.y, b.y);
        }

        public abstract class Result {
        }

        public final class NoIntersection extends Result {
        }

        public final class SingleIntersection extends Result {
            public final long x, y, z;

            private SingleIntersection(long x, long y, long z) {
                long g;
                if(x == 0 && y == 0)
                    g = z;
                else if(x == 0)
                    g = gcd(y, z);
                else if(y == 0)
                    g = gcd(x, z);
                else
                    g = gcd(gcd(x, y), z);
                if(z / g < 0)
                    g *= -1;
                this.x = x / g;
                this.y = y / g;
                this.z = z / g;
            }

            private long gcd(long a, long b) {
                long max = Math.abs(a), min = Math.abs(b);
                while (max > 0) {
                    if (max < min) {
                        long x = max;
                        max = min;
                        min = x;
                    }
                    max %= min;
                }
                return min;
            }

            @Override
            public boolean equals(Object o) {
                if(this == o)
                    return true;
                if(o == null || getClass() != o.getClass())
                    return false;
                SingleIntersection that = (SingleIntersection) o;
                return x == that.x && y == that.y && z == that.z;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y, z);
            }
        }

        public final class InfiniteIntersection extends Result {
            private InfiniteIntersection() {}
        }
    }

    public static class Vector {
        public final long x, y;

        public Vector(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public Vector add(Vector that) {
            return new Vector(this.x + that.x, this.y + that.y);
        }

        public Vector subtract(Vector that) {
            return new Vector(this.x - that.x, this.y - that.y);
        }

        public Vector multiply(int k) {
            return new Vector(this.x * k, this.y * k);
        }

        public long dot(Vector that) {
            return this.x * that.x + this.y * that.y;
        }

        public long cross(Vector that) {
            return this.x * that.y - this.y * that.x;
        }

        public double angle(Vector that) {
            return Math.atan2(this.cross(that), this.dot(that));
        }

        public double length() {
            return Math.sqrt(dot(this));
        }

        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            Vector vector = (Vector) o;
            return x == vector.x && y == vector.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
