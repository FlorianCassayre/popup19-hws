package popup.hw10;

import java.util.*;

public class SetStack {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int t = Integer.parseInt(scanner.nextLine());
        for(int c = 0; c < t; c++) {
            final int n = Integer.parseInt(scanner.nextLine());

            final ArrayDeque<S> stack = new ArrayDeque<>();

            final Map<S, Integer> dictionary = new HashMap<>();

            for(int i = 0; i < n; i++) {
                final String op = scanner.nextLine();

                switch(op) {
                    case "PUSH":
                        stack.push(S.EMPTY);
                        break;
                    case "DUP":
                        stack.push(stack.peek());
                        break;
                    case "UNION": {
                        final S a = stack.pop(), b = stack.pop();
                        final Set<Integer> set = new HashSet<>(a.elements);
                        set.addAll(b.elements);
                        stack.push(new S(set));
                        break;
                    }
                    case "INTERSECT": {
                        final S a = stack.pop(), b = stack.pop();
                        final Set<Integer> set = new HashSet<>(a.elements);
                        set.retainAll(b.elements);
                        stack.push(new S(set));
                        break;
                    }
                    case "ADD": {
                        final S a = stack.pop(), b = stack.pop();
                        final Set<Integer> set = new HashSet<>(b.elements);
                        if(!dictionary.containsKey(a))
                            dictionary.put(a, dictionary.size());
                        set.add(dictionary.get(a));
                        stack.push(new S(set));
                        break;
                    }
                    default:
                        throw new IllegalStateException();
                }

                System.out.println(stack.peek().elements.size());
            }

            System.out.println("***");
        }

        scanner.close();
    }

    private static final class S {
        public static final S EMPTY = new S(Collections.emptySet());

        private final Set<Integer> elements;

        private S(Set<Integer> elements) {
            this.elements = elements;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            S s = (S) o;
            return Objects.equals(elements, s.elements);
        }

        @Override
        public int hashCode() {
            return elements.hashCode();
        }
    }
}
