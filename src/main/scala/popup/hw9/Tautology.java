package popup.hw9;

import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Tautology {
    // For the sake of the exercise...

    private static final BinaryOperator<Function<Arguments, Boolean>>
            AND = (a, b) -> x -> a.apply(x) && b.apply(x),
            OR = (a, b) -> x -> a.apply(x) || b.apply(x),
            IMPLIES = (a, b) -> x -> !(a.apply(x) && !b.apply(x)),
            EQUALS = (a, b) -> x -> a.apply(x) == b.apply(x);
    private static final UnaryOperator<Function<Arguments, Boolean>>
            NOT = a -> x -> !a.apply(x);

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        while(true) {
            final String string = scanner.nextLine();
            if(string.equals("0"))
                break;

            final Function<Arguments, Boolean> f = construct(string).f;

            boolean forall = true;
            for(int i = 0; i < 1 << 5 && forall; i++) {
                final Arguments arguments = new Arguments((i & 1) != 0, ((i >> 1) & 1) != 0, ((i >> 2) & 1) != 0, ((i >> 3) & 1) != 0, ((i >> 4) & 1) != 0);

                if(!f.apply(arguments))
                    forall = false;
            }

            System.out.println(forall ? "tautology" : "not");
        }

        scanner.close();
    }

    private static Result construct(String string) {
        final char c = string.charAt(0);
        if(Character.isUpperCase(c)) { // Operator
            final Result arg1 = construct(string.substring(1));
            if(c == 'N') { // Unary
                return new Result(NOT.apply(arg1.f), arg1.string);
            } else { // Binary
                final Result arg2 = construct(arg1.string);

                final Function<Arguments, Boolean> a = arg1.f, b = arg2.f, f;
                if(c == 'K') f = AND.apply(a, b);
                else if(c == 'A') f = OR.apply(a, b);
                else if(c == 'C') f = IMPLIES.apply(a, b);
                else if(c == 'E') f = EQUALS.apply(a, b);
                else throw new IllegalStateException();

                return new Result(f, arg2.string);
            }
        } else {
            final Function<Arguments, Boolean> f;
            if(c == 'p') f = x -> x.p;
            else if(c == 'q') f = x -> x.q;
            else if(c == 'r') f = x -> x.r;
            else if(c == 's') f = x -> x.s;
            else if(c == 't') f = x -> x.t;
            else throw new IllegalStateException();

            return new Result(f, string.substring(1));
        }
    }

    private static final class Result {
        public final Function<Arguments, Boolean> f;
        public final String string;

        private Result(Function<Arguments, Boolean> f, String string) {
            this.f = f;
            this.string = string;
        }
    }

    private static final class Arguments {
        public final boolean p, q, r, s, t;

        private Arguments(boolean p, boolean q, boolean r, boolean s, boolean t) {
            this.p = p;
            this.q = q;
            this.r = r;
            this.s = s;
            this.t = t;
        }
    }
}
