package popup.hw11;

import java.util.Locale;
import java.util.Scanner;

public class CountingTriangles {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        while(true) {
            final int n = scanner.nextInt();

            if(n == 0)
                break;

            final Line[] lines = new Line[n];
            for(int i = 0; i < n; i++) {
                final Line line = new Line(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                lines[i] = line;
            }

            int count = 0;
            for(int i = 0; i < n; i++) {
                final Line l1 = lines[i];
                for(int j = i + 1; j < n; j++) {
                    final Line l2 = lines[j];
                    if(l1.intersects(l2))
                        for(int k = j + 1; k < n; k++) {
                            final Line l3 = lines[k];
                            if(l1.intersects(l3) && l2.intersects(l3))
                                count++;
                        }
                }
            }

            System.out.println(count);
        }

        scanner.close();
    }

    private static final class Line {
        public final double x1, y1, x2, y2;
        public final double lx, ly, lz;

        private Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;

            this.lx = y2 - y1;
            this.ly = x1 - x2;
            this.lz = -(lx * x1 + ly * y1);
        }

        public boolean intersects(Line that) {
            final double precision = 1E-30;
            final double cx = this.ly * that.lz - this.lz * that.ly, cy = this.lz * that.lx - this.lx * that.lz, cz = this.lx * that.ly - this.ly * that.lx;

            if(Math.abs(cz) < precision)
                return false;

            final double x = cx / cz, y = cy / cz;

            return this.inside(x, y) && that.inside(x, y);
        }

        private boolean inside(double x, double y) {
            return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
        }
    }
}
