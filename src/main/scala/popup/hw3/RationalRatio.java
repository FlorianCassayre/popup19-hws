package popup.hw3;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Scanner;

public class RationalRatio {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String[] decimal = scanner.next().split("\\.");

        final int repeating = scanner.nextInt(), index = decimal[1].length() - repeating;

        final BigInteger integer = new BigInteger(decimal[0] + decimal[1].substring(0, index));
        final BigInteger period = new BigInteger(decimal[1].substring(index));

        final BigRational number = new BigRational(integer).add(
                new BigRational(period).divide(BigRational.TEN.pow(repeating).subtract(BigRational.ONE))
        );

        final BigRational finalNumber = number.divide(BigRational.TEN.pow(index));

        System.out.println(finalNumber);

        scanner.close();
    }

    /**
     * Representation of an arbitrarily big rational number.
     */
    private static final class BigRational extends Number implements Comparable<BigRational> {
        public static final BigRational ZERO = valueOf(0);
        public static final BigRational ONE = valueOf(1);
        public static final BigRational MINUS_ONE = valueOf(-1);
        public static final BigRational TEN = valueOf(10);

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

        public static BigRational valueOf(int numerator, int denominator) {
            return new BigRational(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        }

        public static BigRational valueOf(int numerator) {
            return valueOf(numerator, 1);
        }
    }
}
