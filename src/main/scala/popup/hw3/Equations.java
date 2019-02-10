package popup.hw3;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Scanner;

public class Equations {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int order = 2;

        final int n = Integer.parseInt(scanner.nextLine());

        for(int i = 0; i < n; i++) {
            final String line1 = scanner.nextLine(), line2 = scanner.nextLine();
            final BigRational[][] matrix = new BigRational[order][order + 1];
            for(int j = 0; j < order; j++)
                for(int k = 0; k < order + 1; k++)
                    matrix[j][k] = BigRational.ZERO;

            fill(matrix[0], line1);
            fill(matrix[1], line2);

            gaussJordan(matrix);

            boolean possible = true;
            for(int j = 0; j < order && possible; j++) {
                boolean isZero = true;
                for(int k = 0; k < order && isZero; k++)
                    if(!matrix[j][k].equals(BigRational.ZERO))
                        isZero = false;
                if(isZero && !matrix[j][order].equals(BigRational.ZERO))
                    possible = false;
            }

            for(int j = 0; j < order; j++) {
                int index = -1;
                for(int k = 0; k < order && index == -1; k++)
                    if(!matrix[k][j].equals(BigRational.ZERO))
                        index = k;
                int count = 0;
                if(index != -1) {
                    for(int k = 0; k < order; k++)
                        if(!matrix[index][k].equals(BigRational.ZERO))
                            count++;
                }
                if(index != -1 && possible && count == 1) {
                    final BigRational br = matrix[index][order];
                    System.out.println(br.isInteger() ? br.getNumerator() : br);
                }
                else
                    System.out.println("don't know");
            }

            if(i < n - 1) {
                System.out.println();
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void fill(BigRational[] matrix, String equation) {
        final String[] split = equation.split(" ");

        boolean sign = false;
        for(int i = 0; i < split.length; i++) {
            final String s = split[i];
            if(s.equals("=")) {
                sign = true;
            } else if(!s.equals("+")) {
                final char last = s.charAt(s.length() - 1);
                String head = s.substring(0, s.length() - 1);
                if(head.isEmpty() || head.equals("-"))
                    head += "1";
                final int j;
                final BigRational add;
                if(last == 'x') {
                    j = 0;
                    add = BigRational.valueOf(Integer.parseInt(head));
                } else if(last == 'y') {
                    j = 1;
                    add = BigRational.valueOf(Integer.parseInt(head));
                } else {
                    j = 2;
                    add = BigRational.ZERO.subtract(BigRational.valueOf(Integer.parseInt(s)));
                }
                final BigRational signed = sign ? BigRational.ZERO.subtract(add) : add;
                matrix[j] = matrix[j].add(signed);
            }
        }
    }

    public static void gaussJordan(BigRational[][] a) {
        final int n = 2;
        int r = -1;
        for(int j = 0; j < n; j++) {
            int k = j;
            for(int i = j + 1; i < n; i++)
                if(a[i][j].abs().compareTo(a[k][j].abs()) > 0)
                    k = i;
            if(!a[k][j].equals(BigRational.ZERO)) {
                r++;
                final BigRational val = a[k][j];
                for(int i = 0; i < n + 1; i++)
                    a[k][i] = a[k][i].divide(val);
                for(int i = 0; i < n + 1; i++) {
                    final BigRational temp = a[k][i];
                    a[k][i] = a[r][i];
                    a[r][i] = temp;
                }
                for(int i = 0; i < n; i++) {
                    final BigRational v = a[i][j];
                    if(i != r)
                        for(int l = 0; l < n + 1; l++)
                            a[i][l] = a[i][l].subtract(a[r][l].multiply(v));
                }
            }
        }
    }

    private static final class BigRational extends Number implements Comparable<BigRational> {
        public static final BigRational ZERO = valueOf(0);
        public static final BigRational ONE = valueOf(1);
        public static final BigRational MINUS_ONE = valueOf(-1);

        private final BigInteger numerator;
        private final BigInteger denominator;

        /**
         * Constructs a new canonical rational number from the numerator an the denominator.
         * @param numerator the numerator
         * @param denominator the denominator
         */
        public BigRational(BigInteger numerator, BigInteger denominator) {
            if(denominator.equals(BigInteger.ZERO))
                throw new ArithmeticException();

            boolean sign = false;
            if(numerator.signum() >= 0 ^ denominator.signum() >= 0) {
                sign = true;
            } else {
                if(numerator.equals(BigInteger.ZERO)) {
                    denominator = BigInteger.ONE;
                }
            }

            numerator = numerator.abs();
            denominator = denominator.abs();

            if(sign) {
                numerator = numerator.multiply(BigInteger.valueOf(-1));
            }

            final BigInteger divisor = gcd(numerator.abs(), denominator);
            if(!divisor.equals(BigInteger.ONE)) {
                numerator = numerator.divide(divisor);
                denominator = denominator.divide(divisor);
            }

            this.numerator = numerator;
            this.denominator = denominator;
        }

        /**
         * Constructs a rational number from an integer.
         * The denominator will be set to one.
         * @param numerator the numerator
         */
        public BigRational(BigInteger numerator) {
            this(numerator, BigInteger.ONE);
        }

        @Override
        public int intValue() {
            return numerator.intValue() / denominator.intValue();
        }

        @Override
        public long longValue() {
            return numerator.longValue() / denominator.longValue();
        }

        @Override
        public float floatValue() {
            return numerator.floatValue() / denominator.floatValue();
        }

        @Override
        public double doubleValue() {
            return numerator.doubleValue() / denominator.doubleValue();
        }

        public BigInteger getNumerator() {
            return numerator;
        }

        public BigInteger getDenominator() {
            return denominator;
        }

        public BigRational add(BigRational n) {
            return new BigRational(numerator.multiply(n.denominator).add(n.numerator.multiply(denominator)), denominator.multiply(n.denominator));
        }

        public BigRational subtract(BigRational n) {
            return add(new BigRational(n.numerator.multiply(BigInteger.valueOf(-1)), n.denominator));
        }

        public BigRational multiply(BigRational n) {
            return new BigRational(numerator.multiply(n.numerator), denominator.multiply(n.denominator));
        }

        public BigRational divide(BigRational n) {
            return multiply(n.reciprocal());
        }

        public BigRational reciprocal() {
            return new BigRational(denominator, numerator);
        }

        public boolean isInteger() {
            return denominator.equals(BigInteger.ONE);
        }

        public BigRational abs() {
            return new BigRational(numerator.abs(), denominator);
        }

        public BigRational pow(int n) {
            if(n < 0)
                throw new IllegalArgumentException();

            BigRational b = ONE;

            for(int i = 0; i < n; i++) {
                b = b.multiply(this);
            }

            return b;
        }

        private BigInteger gcd(BigInteger a, BigInteger b) {
            BigInteger r;
            while(!b.equals(BigInteger.ZERO)) {
                r = a.mod(b);
                a = b;
                b = r;
            }
            return a;
        }

        @Override
        public int compareTo(BigRational o) {
            BigInteger a = numerator.multiply(o.denominator);
            BigInteger b = denominator.multiply(o.numerator);

            return a.compareTo(b);
        }

        @Override
        public String toString() {
            return numerator.toString() + "/" + denominator.toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(numerator, denominator);
        }

        @Override
        public boolean equals(Object object) {
            if(object instanceof BigRational) {
                BigRational n = (BigRational) object;
                return numerator.equals(n.numerator) && denominator.equals(n.denominator);
            }
            return false;
        }

        public static BigRational valueOf(long numerator, long denominator) {
            return new BigRational(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        }

        public static BigRational valueOf(long numerator) {
            return valueOf(numerator, 1);
        }
    }
}
