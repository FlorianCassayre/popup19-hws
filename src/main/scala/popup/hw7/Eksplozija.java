package popup.hw7;

import java.util.*;

public class Eksplozija {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

         String string = scanner.nextLine(), explosion = scanner.nextLine();

        final Deque<Sequence> stack = new ArrayDeque<>();

        int i = 0;
        Sequence current = new Sequence(0);
        while(true) {
            if(current.isPrefix && current.len == explosion.length()) { // String explodes
                if(stack.isEmpty() || !stack.peek().isPrefix)
                    current = new Sequence(i);
                else
                    current = stack.pop();
            }
            if(i >= string.length()) { // Exit condition
                if(current.len > 0)
                    stack.push(current);
                break;
            }
            if(current.len == 0) {
                current.isPrefix = string.charAt(i) == explosion.charAt(0);
                current.len++;
            } else if(current.isPrefix) {
                if(string.charAt(i) == explosion.charAt(current.len))
                    current.len++;
                else {
                    stack.push(current);
                    current = new Sequence(i, 1, string.charAt(i) == explosion.charAt(0));
                }
            } else {
                if(string.charAt(i) != explosion.charAt(0)) {
                    current.len++;
                } else {
                    stack.push(current);
                    current = new Sequence(i, 1, true);
                }
            }
            i++;
        }

        final Iterator<Sequence> it = stack.descendingIterator();
        final StringBuilder builder = new StringBuilder();
        while(it.hasNext()) {
            final Sequence seq = it.next();
            if(seq.isPrefix)
                builder.append(explosion, 0, seq.len);
            else
                builder.append(string, seq.start, seq.start + seq.len);
        }

        final String result = builder.toString();
        System.out.println(!result.isEmpty() ? result : "FRULA");

        scanner.close();
    }

    private static class Sequence {
        public int start, len;
        public boolean isPrefix;

        public Sequence(int start, int len, boolean isPrefix) {
            this.start = start;
            this.len = len;
            this.isPrefix = isPrefix;
        }

        public Sequence(int i) {
            this(i, 0, false);
        }

        @Override
        public String toString() {
            return "{" + isPrefix + ", " + start + ", " + len + "}";
        }
    }
}
