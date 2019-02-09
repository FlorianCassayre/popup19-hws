package popup.hw3;

import java.math.BigInteger;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Fraction {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int n1 = scanner.nextInt(), n2 = scanner.nextInt();
        final long[] r1 = new long[n1], r2 = new long[n2];

        for(int i = 0; i < n1; i++)
            r1[i] = scanner.nextInt();
        for(int i = 0; i < n2; i++)
            r2[i] = scanner.nextInt();

        final BigRational b1 = continued2rational(r1), b2 = continued2rational(r2);

        // +, -, *, /
        final List<BinaryOperator<BigRational>> operations =
                Arrays.asList(BigRational::add, BigRational::subtract, BigRational::multiply, BigRational::divide);

        for(BinaryOperator<BigRational> op : operations) {
            final long[] result = rational2continued(op.apply(b1, b2));

            for(int i = 0; i < result.length; i++) {
                System.out.print(result[i]);
                if(i < result.length - 1)
                    System.out.print(" ");
                else
                    System.out.println();
            }
        }

        scanner.close();
    }

    private static BigRational continued2rational(long[] sequence) {
        BigRational acc = BigRational.ZERO;
        for(int i = sequence.length - 1; i >= 0; i--) {
            final BigRational prev = acc;
            acc = BigRational.valueOf(sequence[i]);
            if(i < sequence.length - 1)
                acc = acc.add(prev.reciprocal());
        }
        return acc;
    }

    private static long[] rational2continued(BigRational rational) {
        BigRational x = rational;

        final List<Long> sequence = new ArrayList<>();
        while(!x.isInteger()) {
            final BigInteger a = x.getNumerator().divide(x.getDenominator());
            x = x.subtract(new BigRational(a)).reciprocal();
            sequence.add(a.longValue());
        }
        sequence.add(x.longValue());

        final long[] array = new long[sequence.size()];
        for(int i = 0; i < array.length; i++)
            array[i] = sequence.get(i);

        return array;
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
